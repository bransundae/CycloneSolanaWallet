package com.cyclone.solana.cyclonesolanawallet.usecase.solana

import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.Result
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.repository.interfaces.KeyPairRepository
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SolTransferTransaction
import kotlinx.coroutines.flow.*
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters

class SendSolUsecase(
    private val keyPairRepository: KeyPairRepository,
    private val solanaRPCRepository: SolanaRPCRepository,
    private val getRecentBlockHashUsecase: GetRecentBlockHashUsecase
    ) {
    suspend operator fun invoke(
        fromAddress: String,
        toAddress: String,
        lamports: Int
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>? {
        val signer = keyPairRepository.getKeyPair(fromAddress).firstOrNull() ?: return null

        val blockHash = getRecentBlockHash()

        return if (!blockHash.isNullOrBlank()) {
            val transaction = SolTransferTransaction.invoke(
                fromAddress = fromAddress,
                toAddress = toAddress,
                blockhash = blockHash,
                lamports = lamports
            )

            val asymmetricCipherKeyPair = getEd25519KeyPairFromPrivateKey(
                signer.privateKey
            )

            val serialisedTransaction = transaction.apply {
                sign(keyPairs = listOf(asymmetricCipherKeyPair))
            }.serialise()

            solanaRPCRepository.sendTransaction(
                Base58Encoder.invoke(serialisedTransaction)
            )
        } else null
    }

    private suspend fun getRecentBlockHash(): String? {
        val blockHash = getRecentBlockHashUsecase
            .invoke()
            .filterNot { it == NetworkResource.Loading }
            .first()

        return if (blockHash is NetworkResource.Success) {
            val value = blockHash.result.specificResult as Result.StringResult
            value.value
        } else null
    }

    private fun getEd25519KeyPairFromPrivateKey(byteArray: ByteArray): AsymmetricCipherKeyPair {
        val privateKey = Ed25519PrivateKeyParameters(byteArray)
        val publicKey = privateKey.generatePublicKey()

        return AsymmetricCipherKeyPair(
            publicKey, privateKey
        )
    }
}