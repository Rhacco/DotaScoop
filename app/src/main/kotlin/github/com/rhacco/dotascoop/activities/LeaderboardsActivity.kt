package github.com.rhacco.dotascoop.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.base.BaseNavigationDrawerActivity
import github.com.rhacco.dotascoop.fragments.LeaderboardsFragment
import kotlinx.android.synthetic.main.view_pager.*

class LeaderboardsActivity : BaseNavigationDrawerActivity() {
    private var mCurrentTabPosition = 0
    private lateinit var mAdapter: CustomPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager)
        super.initNavigationDrawer(drawer_layout)
        mAdapter = CustomPagerAdapter(supportFragmentManager)
        view_pager.adapter = mAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                mAdapter.mAllFragments[mCurrentTabPosition].showAllEntries()
                mCurrentTabPosition = position
            }

            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
        })
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (App.sSharedPreferences.getDefaultLeaderboard()) {
            getString(R.string.region_europe) -> view_pager.setCurrentItem(1, false)
            getString(R.string.region_se_asia) -> view_pager.setCurrentItem(2, false)
            getString(R.string.region_china) -> view_pager.setCurrentItem(3, false)
        }
        menuInflater.inflate(R.menu.search_view, menu)
        val searchMenuItem = menu?.findItem(R.id.search)
        val searchView = searchMenuItem?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setPadding(-30, 15, 20, 15)
        searchView.setIconifiedByDefault(false)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnCloseListener { handleSearchClosed() }
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean = handleSearchClosed()

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean = true
        })
        return true
    }

    override fun onNewIntent(intent: Intent) = handleIntent(intent)

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_SEARCH)
            mAdapter.mAllFragments[mCurrentTabPosition].handleSearchQuery(
                    intent.getStringExtra(SearchManager.QUERY))
    }

    private fun handleSearchClosed(): Boolean {
        mAdapter.mAllFragments[mCurrentTabPosition].showAllEntries()
        return true
    }

    private class CustomPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        val mAllFragments: MutableList<LeaderboardsFragment> = mutableListOf()

        override fun getItem(position: Int): Fragment {
            val fragment = LeaderboardsFragment()
            val bundle = Bundle()
            bundle.putInt("tab_position", position)
            fragment.arguments = bundle
            mAllFragments.add(fragment)
            return fragment
        }

        override fun getCount(): Int = 4

        override fun getPageTitle(position: Int): CharSequence =
                when (position) {
                    0 -> App.instance.getString(R.string.region_americas)
                    1 -> App.instance.getString(R.string.region_europe)
                    2 -> App.instance.getString(R.string.region_se_asia)
                    else -> App.instance.getString(R.string.region_china)
                }
    }
}