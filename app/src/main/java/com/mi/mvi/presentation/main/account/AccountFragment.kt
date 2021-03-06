package com.mi.mvi.presentation.main.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.presentation.main.account.state.AccountEventState
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AccountFragment : BaseAccountFragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        subscribeObservers()
        viewModel.setEventState(AccountEventState.GetAccountEvent)

        change_password.setOnClickListener { findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment) }
        logout_button.setOnClickListener { viewModel.logout() }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                dataStateChangeListener?.onDataStateChangeListener(dataState)
                dataState.data?.let { viewState ->
                    viewState.userEntity?.let { account ->
                        viewModel.setAccountData(account)
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.userEntity?.let { account ->
                email.text = account.email
                username.text = account.username
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
