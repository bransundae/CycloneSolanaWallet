package com.cyclone.solana.cyclonesolanawallet.usecase.wallet

import com.cyclone.solana.core.repository.interfaces.SeedRepository
import com.cyclone.solana.core.usecase.SeedDeriver
import kotlinx.coroutines.flow.firstOrNull
import org.bouncycastle.crypto.AsymmetricCipherKeyPair

class DeriveKeyPairsUsecase(private val seedRepository: SeedRepository) {
    suspend operator fun invoke(): List<AsymmetricCipherKeyPair>? {
        val seed = seedRepository.getSeed().firstOrNull()

        return if (seed != null)
            SeedDeriver.invoke(seed.seed)
        else null
    }
}