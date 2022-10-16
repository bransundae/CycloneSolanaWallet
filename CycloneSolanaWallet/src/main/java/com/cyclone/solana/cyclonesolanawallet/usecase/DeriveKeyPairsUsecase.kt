package com.cyclone.solana.cyclonesolanawallet.usecase

import com.cyclone.solana.core.repository.interfaces.SeedRepository
import com.cyclone.solana.core.usecase.SeedDeriver
import kotlinx.coroutines.flow.firstOrNull

class DeriveKeyPairsUsecase(private val seedRepository: SeedRepository) {
    suspend operator fun invoke() {
        val seed = seedRepository.getSeed().firstOrNull()

        if (seed != null) {
            val keyPairs = SeedDeriver.invoke(seed.seed)
        }
    }
}