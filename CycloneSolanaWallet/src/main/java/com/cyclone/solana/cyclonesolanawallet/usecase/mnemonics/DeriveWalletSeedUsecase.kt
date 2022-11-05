package com.cyclone.solana.cyclonesolanawallet.usecase.mnemonics

import com.cyclone.solana.core.datamodel.entity.Seed
import com.cyclone.solana.core.extensions.binaryToHex
import com.cyclone.solana.core.extensions.toBinaryString
import com.cyclone.solana.core.repository.interfaces.SeedRepository
import com.cyclone.solana.core.usecase.MnemonicDecoder

class DeriveWalletSeedUsecase(private val seedRepository: SeedRepository) {
    suspend operator fun invoke(mnemonics: List<String>): Boolean {
        val entropy = MnemonicDecoder.invoke(mnemonics)

        return if (entropy != null) {
            seedRepository.saveSeeds(
                Seed(
                    hex = entropy.toBinaryString().binaryToHex(),
                    seed = entropy
                )
            )

            true
        } else false
    }
}