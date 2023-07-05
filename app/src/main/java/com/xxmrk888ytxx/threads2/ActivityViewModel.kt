package com.xxmrk888ytxx.threads2

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ActivityViewModel : ViewModel() {

    private val maxCapacity = 200

    val pierWithBread = MutableStateFlow(0)

    val pierWithBanana = MutableStateFlow(0)

    val pierWithClothes = MutableStateFlow(0)


    private val breadQueue = Channel<Ship>(
        capacity = 100
    )

    private val bananaQueue = Channel<Ship>(capacity = 100)

    private val clothesQueue = Channel<Ship>(capacity = 100)

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun newShip(ship: Ship) {
        when(ship) {
            is Ship.Bread -> {
                if(!breadQueue.isClosedForSend) {
                    breadQueue.send(ship)
                }
            }

            is Ship.Banana -> {
                if(!bananaQueue.isClosedForSend) {
                    bananaQueue.send(ship)
                }
            }
            is Ship.Clothes -> {
                if(!clothesQueue.isClosedForSend) {
                    clothesQueue.send(ship)
                }
            }
        }
    }

    private val isAllPiersLoaded : Boolean
        get() = listOf(pierWithBread,pierWithBanana,pierWithClothes).all { it.value >= maxCapacity }


    fun start(context:Context) {
        viewModelScope.launch(Dispatchers.Default) {
            while (
                isActive && !isAllPiersLoaded
            ) {
                val capacity = when(Random(System.currentTimeMillis()).nextInt(0,3)) {
                    0 -> 10
                    1 -> 50
                    2 -> 100
                    else -> error("Invalid capacity id")
                }

                val ship = when(Random(System.currentTimeMillis()).nextInt(0,3)) {
                    0 -> Ship.Bread(capacity)
                    1 -> Ship.Banana(capacity)
                    2 -> Ship.Clothes(capacity)
                    else -> error("Invalid ship id")
                }

                newShip(ship)

                delay(500)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context,"All piers loaded",Toast.LENGTH_SHORT).show()
            }


        }

        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                unloadPier(
                    breadQueue,
                    pierWithBread
                )

                if(pierWithBread.value >= maxCapacity) {
                    breadQueue.close()
                    return@launch
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                unloadPier(
                    bananaQueue,
                    pierWithBanana
                )

                if(pierWithBanana.value >= maxCapacity) {
                    bananaQueue.close()
                    return@launch
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                unloadPier(
                    clothesQueue,
                    pierWithClothes
                )

                if(pierWithClothes.value >= maxCapacity) {
                    clothesQueue.close()
                    return@launch
                }
            }
        }
    }

    private suspend fun unloadPier(
        channel:Channel<Ship>,
        pierCapacity:MutableStateFlow<Int>
    ) {
        val ship = channel.receive()

        var shipCapacity = ship.capacity

        while (shipCapacity != 0) {
            if(pierCapacity.value >= maxCapacity)
                break

            pierCapacity.update { minOf(maxCapacity,it + 10) }

            shipCapacity -= 10

            delay(1000)
        }
    }



}