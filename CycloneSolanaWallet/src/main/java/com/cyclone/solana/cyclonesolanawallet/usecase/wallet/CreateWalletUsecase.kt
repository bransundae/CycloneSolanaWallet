package com.cyclone.solana.cyclonesolanawallet.usecase.wallet

import com.cyclone.solana.core.datamodel.dto.solanaRPC.Result
import com.cyclone.solana.core.datamodel.entity.KeyPair
import com.cyclone.solana.core.extensions.toEd25519PrivateKeyKeyParameters
import com.cyclone.solana.core.extensions.toEd25519PublicKeyParameters
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.repository.interfaces.KeyPairRepository
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.cyclonesolanawallet.usecase.solana.GetSolBalanceUsecase
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class CreateWalletUsecase(
    private val keyPairRepository: KeyPairRepository,
    private val deriveKeyPairsUsecase: DeriveKeyPairsUsecase,
    private val getSolBalanceUsecase: GetSolBalanceUsecase
    ) {
    suspend operator fun invoke(): String? {
        val derivedKeyPairs = deriveKeyPairsUsecase.invoke()

        return if (!derivedKeyPairs.isNullOrEmpty()) {
            val keyPairs = keyPairRepository
                .getAllKeyPairs()
                .firstOrNull()

            derivedKeyPairs.find { derived ->
                val derivedPubKey = Base58Encoder.invoke(
                    derived.public.toEd25519PublicKeyParameters.encoded
                )

                val newKeyPair = keyPairs?.find {
                    it.publicKey == derivedPubKey
                }

                return when {
                    newKeyPair != null -> null
                    !keyPairHasBalance(derivedPubKey) -> derivedPubKey
                    else -> null
                }
            }?.let {
                val keyPair = KeyPair(
                    publicKey = Base58Encoder.invoke(
                        it.public.toEd25519PublicKeyParameters.encoded
                    ),
                    privateKey = it.private.toEd25519PrivateKeyKeyParameters.encoded
                )

                keyPairRepository.saveKeyPairs(keyPair)

                keyPair.publicKey
            }
        } else null
    }

    private suspend fun keyPairHasBalance(address: String): Boolean {
        val balance = getSolBalanceUsecase
            .invoke(address)
            .filterNot { it == NetworkResource.Loading }
            .first()

        return when {
            balance is NetworkResource.Error -> false
            balance is NetworkResource.Success && (balance.result.specificResult as Result.LongResult).value == 0L -> false
            else -> true
        }
    }
}