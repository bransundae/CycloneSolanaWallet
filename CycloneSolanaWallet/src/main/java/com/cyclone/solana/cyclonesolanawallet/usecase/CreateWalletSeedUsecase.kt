package com.cyclone.solana.cyclonesolanawallet.usecase

import com.cyclone.solana.core.datamodel.entity.Seed
import com.cyclone.solana.core.extensions.binaryToHex
import com.cyclone.solana.core.extensions.toBinaryString
import com.cyclone.solana.core.repository.interfaces.SeedRepository
import java.security.SecureRandom

class CreateWalletSeedUsecase(private val seedRepository: SeedRepository) {
    suspend operator fun invoke() {
        val entropy = SecureRandom().generateSeed(16)

        seedRepository.saveSeeds(
            Seed(
                hex = entropy.toBinaryString().binaryToHex(),
                seed = entropy
            )
        )
    }
}