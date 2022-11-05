package com.cyclone.solana.cyclonesolanawallet.usecase.mnemonics

import com.cyclone.solana.core.repository.interfaces.SeedRepository
import com.cyclone.solana.core.usecase.MnemonicEncoder
import kotlinx.coroutines.flow.firstOrNull

class DeriveMnemonicsUsecase(private val seedRepository: SeedRepository) {
    suspend operator fun invoke(): List<String>? {
        val seed = seedRepository.getSeed().firstOrNull()

        return if (seed != null) {
            MnemonicEncoder.invoke(seed.seed)
        } else null
    }
}