HK News Android
===============

[![Release](https://img.shields.io/github/release/ayltai/hknews-android.svg?style=flat)](https://1544-77390316-gh.circle-artifacts.com/0/apk/app-release.apk)
[![Build](https://img.shields.io/circleci/project/github/ayltai/hknews-android/master.svg?style=flat)](https://circleci.com/gh/ayltai/hknews-android)
[![Code Quality](https://img.shields.io/codacy/grade/05a4a29b58154cd28a472dd8f7f6a874.svg?style=flat)](https://app.codacy.com/app/AlanTai/hknews-android/dashboard)
[![Code Coverage](https://img.shields.io/codacy/coverage/05a4a29b58154cd28a472dd8f7f6a874.svg?style=flat)](https://app.codacy.com/app/AlanTai/hknews-android/dashboard)
[![Code Coverage](https://img.shields.io/codecov/c/github/ayltai/hknews-android.svg?style=flat)](https://codecov.io/gh/ayltai/hknews-android)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/2791/badge)](https://bestpractices.coreinfrastructure.org/projects/2791)
[![Android API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat&label=API&maxAge=300)](https://www.android.com/history/)
[![License](https://img.shields.io/github/license/ayltai/hknews-android.svg?style=flat)](https://github.com/ayltai/hknews-android/blob/master/LICENSE)

Serves aggregated news from 10+ local news publishers in Hong Kong. Made with ❤

![Screenshot (Compact)](design/screenshot_cozy_light_framed.png "Screenshot (Cozy)") ![Screenshot (Dark)](design/screenshot_compact_dark_framed.png "Screenshot (Dark)") ![Screenshot (Details)](design/screenshot_details_framed.png "Screenshot (Details)")

## Features
* Read news from 10 local news publishers
* Support video news
* Bookmarks and reading history
* No ads. We hate ads as much as you do

## News Publishers
* [Apple Daily (蘋果日報)](http://hk.apple.nextmedia.com)
* [Oriental Daily (東方日報)](http://orientaldaily.on.cc)
* [Sing Tao (星島日報)](http://std.stheadline.com)<sup>1</sup>
* [Hong Kong Economic Times (經濟日報)](http://www.hket.com)<sup>2</sup>
* [Sing Pao (成報)](https://www.singpao.com.hk)
* [Ming Pao (明報)](http://www.mingpao.com)
* [Headline (頭條日報)](http://hd.stheadline.com)<sup>1</sup>
* [Sky Post (晴報)](http://skypost.ulifestyle.com.hk)
* [Hong Kong Economic Journal (信報)](http://www.hkej.com)
* [RTHK (香港電台)](http://news.rthk.hk)
* [South China Morning Post (南華早報)](http://www.scmp.com/frontpage/hk)<sup>3</sup>
* [The Standard (英文虎報)](http://www.thestandard.com.hk)
* [Wen Wei Po (文匯報)](http://news.wenweipo.com)

###### Notes
1. Scrapping might be blocked by their servers
2. Full news details may be available to members only
3. Topics section cannot be parsed correctly

## HK News Backend
[hknews-backend](https://github.com/ayltai/hknews-backend)

## Requirements
This app supports Android 4.1 Jelly Bean (API 16) or later.

## Acknowledgements
This app is made with the support of open source projects:

* [Flow](https://github.com/square/flow)
* [Realm](https://realm.io/news/realm-for-android)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxJava2Debug](https://github.com/akaita/RxJava2Debug)
* [Dagger 2](https://google.github.io/dagger)
* [Facebook Fresco](https://github.com/facebook/fresco)
* [FrescoImageViewer](https://github.com/stfalcon-studio/FrescoImageViewer)
* [Subsampling Scale Image View](https://github.com/davemorrissey/subsampling-scale-image-view)
* [BigImageViewer](https://github.com/Piasy/BigImageViewer)
* [Material SearchBar](https://github.com/mancj/MaterialSearchBar)
* [ExoPlayer](https://github.com/google/ExoPlayer)
* [ShimmerLayout](https://github.com/team-supercharge/ShimmerLayout)
* [SmallBang](https://github.com/hanks-zyh/SmallBang)
* [OkHttp](https://github.com/square/okhttp)
* [FlowLayout](https://github.com/nex3z/FlowLayout)
* [Calligraphy](https://github.com/InflationX/Calligraphy)
* [AutoValue](https://github.com/google/auto/tree/master/value)
* [Gson](https://github.com/google/gson)
* [Espresso](https://google.github.io/android-testing-support-library)
* [JUnit 4](https://github.com/junit-team/junit4)
* [Mockito](https://github.com/mockito/mockito)
* [PowerMock](https://github.com/powermock/powermock)
* [Robolectric](http://robolectric.org)
* [LeakCanary](https://github.com/square/leakcanary)

… and closed source services:

* [CircleCI](https://circleci.com)
* [Fabric Crashlytics](https://fabric.io/kits/android/crashlytics)
