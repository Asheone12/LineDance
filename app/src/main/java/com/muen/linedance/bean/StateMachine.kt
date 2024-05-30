package com.muen.linedance.bean

object StateMachine {
    enum class StateEnum {
        Menu, OnGame, Result
    }

    private var state: StateEnum? = null
    private val instance: StateMachine? = null

    init{
        this.state = StateEnum.Menu
    }

    fun getState(): StateEnum? {
        return state
    }

    fun setState(state: StateEnum?) {
        this.state = state
    }

    fun moveToNextState(currentState: StateEnum?) {
        when (currentState) {
            StateEnum.Menu -> state = StateEnum.OnGame
            StateEnum.OnGame -> state = StateEnum.Result
            StateEnum.Result -> state = StateEnum.Menu
            else -> {}
        }
    }
}