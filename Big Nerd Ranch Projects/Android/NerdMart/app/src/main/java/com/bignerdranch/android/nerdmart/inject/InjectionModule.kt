package com.bignerdranch.android.nerdmart.inject

import com.bignerdranch.android.nerdmart.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InjectionModule {

    @ContributesAndroidInjector
    abstract fun contributesNerdMartAbstractActivityInjector(): NerdMartAbstractActivity

    @ContributesAndroidInjector
    abstract fun contributesNerdMartAbstractFragmentInjector(): NerdMartAbstractFragment

    @ContributesAndroidInjector
    abstract fun contributesProductsActivityInjector(): ProductsActivity

    @ContributesAndroidInjector
    abstract fun contributesProductsFragmentInjector(): ProductsFragment

    @ContributesAndroidInjector
    abstract fun contributesLoginActivityInjector(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributesLoginFragmentInjector(): LoginFragment
}