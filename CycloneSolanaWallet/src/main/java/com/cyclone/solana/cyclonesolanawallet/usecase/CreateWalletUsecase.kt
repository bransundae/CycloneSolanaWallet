package com.cyclone.solana.cyclonesolanawallet.usecase

import com.cyclone.solana.core.datamodel.entity.KeyPair
import com.cyclone.solana.core.extensions.toEd25519PrivateKeyKeyParameters
import com.cyclone.solana.core.extensions.toEd25519PublicKeyParameters
import com.cyclone.solana.core.repository.interfaces.KeyPairRepository
import com.cyclone.solana.core.repository.interfaces.SeedRepository
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SeedDeriver
import kotlinx.coroutines.flow.firstOrNull

class CreateWalletUsecase(
    private val seedRepository: SeedRepository,
    private val keyPairRepository: KeyPairRepository
    ) {
    suspend fun operator(): String? {
        val seed = seedRepository.getSeed().firstOrNull()

        return if (seed != null) {
            val derivedKeyPairs = SeedDeriver.invoke(seed.seed)

            if (derivedKeyPairs.isNotEmpty()) {
                val keyPairs = keyPairRepository.getAllKeyPairs().firstOrNull()

                derivedKeyPairs.find { derived ->
                    val derivedPubKey = Base58Encoder.invoke(
                        derived.public.toEd25519PublicKeyParameters.encoded
                    )

                    keyPairs?.find {
                        it.publicKey == derivedPubKey
                    } == null

                    // TODO: Query the RPC for balances
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
        } else null
    }

    private fun keyPairHasBalance(): Boolean {
        TODO("Query the RPC for balances")
    }
}