package com.cyclone.solana.cyclonesolanawallet.usecase.solana

import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import kotlinx.coroutines.flow.Flow

class GetSolBalanceUsecase(private val solanaRPCRepository: SolanaRPCRepository) {
    suspend operator fun invoke(
        address: String
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> {
        return solanaRPCRepository.getBalance(
            address
        )
    }
}