HK News Android
===============

| Category      | Measurement                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Pipeline      | [![Build](https://img.shields.io/circleci/project/github/ayltai/hknews-android/master.svg?style=flat)](https://circleci.com/gh/ayltai/hknews-android)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| Quality       | [![Code Quality](https://img.shields.io/codacy/grade/05a4a29b58154cd28a472dd8f7f6a874.svg?style=flat)](https://app.codacy.com/app/AlanTai/hknews-android/dashboard) [![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/ayltai_hknews-android?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android) [![Sonar Violations (short format)](https://img.shields.io/sonar/violations/ayltai_hknews-android?format=short&server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android)                                                                                                                                |
| Coverage      | [![Sonar Test Success Rate](https://img.shields.io/sonar/test_success_density/ayltai_hknews-android?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android) [![Code Coverage](https://img.shields.io/codacy/coverage/05a4a29b58154cd28a472dd8f7f6a874.svg?style=flat)](https://app.codacy.com/app/AlanTai/hknews-android/dashboard) [![Code Coverage](https://img.shields.io/codecov/c/github/ayltai/hknews-android.svg?style=flat)](https://codecov.io/gh/ayltai/hknews-android) [![Sonar Coverage](https://img.shields.io/sonar/coverage/ayltai_hknews-android?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android) |
| Ratings       | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ayltai_hknews-android&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ayltai_hknews-android&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android) [![Sonar Tech Debt](https://img.shields.io/sonar/tech_debt/ayltai_hknews-android?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android)                                                                                                                                  |
| Security      | [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ayltai_hknews-android&metric=security_rating)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android) [![CII Best Practices Tiered Percentage](https://img.shields.io/cii/percentage/2791)](https://bestpractices.coreinfrastructure.org/projects/2791) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ayltai_hknews-android&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=ayltai_hknews-android)                                                                                                                                                                          |
| Miscellaneous | ![Maintenance](https://img.shields.io/maintenance/yes/2020) [![Android API](https://img.shields.io/badge/API-19%2B-blue.svg?style=flat&label=API&maxAge=300)](https://www.android.com/history/) [![Release](https://img.shields.io/github/release/ayltai/hknews-android.svg?style=flat)](https://1544-77390316-gh.circle-artifacts.com/0/apk/app-release.apk) [![License](https://img.shields.io/github/license/ayltai/hknews-android.svg?style=flat)](https://github.com/ayltai/hknews-android/blob/master/LICENSE)                                                                                                                                                                                   |

Serves aggregated news from 10+ local news publishers in Hong Kong. Made with ❤

[![Buy me a coffee](https://img.shields.io/static/v1?label=Buy%20me%20a&message=coffee&color=important&style=for-the-badge&logo=buy-me-a-coffee&logoColor=white)](https://buymeacoff.ee/ayltai)

![Screenshot 1](design/screenshot-1.png "Screenshot 1") ![Screenshot 2](design/screenshot-2.png "Screenshot 2")

![Screenshot 3](design/screenshot-3.png "Screenshot 3") ![Screenshot 4](design/screenshot-4.png "Screenshot 4")

## Features
* Read news from 10 local news publishers
* Support video news
* Bookmarks and reading history
* Deep-learning based smart image cropping
* Support dark mode in Android Q
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

## HK News Web
[hknews-web](https://github.com/ayltai/hknews-web)

## HK News Infrastructure
[hknews-infrastructure](https://github.com/ayltai/hknews-infrastructure)

## Requirements
This app supports Android 4.4 Jelly Bean (API 19) or later.

## Acknowledgements
This app is made with the support of open source projects:

* [Realm](https://realm.io/news/realm-for-android)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxJava2Debug](https://github.com/akaita/RxJava2Debug)
* [Dagger 2](https://google.github.io/dagger)
* [Glimpse](https://github.com/the-super-toys/glimpse-android)
* [TensorFlow Lite](https://www.tensorflow.org/lite)
* [Facebook Fresco](https://github.com/facebook/fresco)
* [FrescoImageViewer](https://github.com/stfalcon-studio/FrescoImageViewer)
* [Subsampling Scale Image View](https://github.com/davemorrissey/subsampling-scale-image-view)
* [BigImageViewer](https://github.com/Piasy/BigImageViewer)
* [Material SearchBar](https://github.com/mancj/MaterialSearchBar)
* [ExoPlayer](https://github.com/google/ExoPlayer)
* [ShimmerLayout](https://github.com/team-supercharge/ShimmerLayout)
* [SmallBang](https://github.com/hanks-zyh/SmallBang)
* [Retrofit](https://github.com/square/retrofit)
* [OkHttp](https://github.com/square/okhttp)
* [Apache Commons Lang3](https://commons.apache.org/proper/commons-lang/)
* [Calligraphy](https://github.com/InflationX/Calligraphy)
* [AutoValue](https://github.com/google/auto/tree/master/value)
* [Gson](https://github.com/google/gson)
* [Espresso](https://google.github.io/android-testing-support-library)
* [JUnit 4](https://github.com/junit-team/junit4)
* [Mockito](https://github.com/mockito/mockito)
* [PowerMock](https://github.com/powermock/powermock)
* [Robolectric](http://robolectric.org)
* [LeakCanary](https://github.com/square/leakcanary)
* [Dexcount Gradle Plugin](https://github.com/KeepSafe/dexcount-gradle-plugin)

… and closed source services:

* [Firebase Remote Config](https://firebase.google.com/docs/remote-config)
* [Firebase Performance Monitoring](https://firebase.google.com/docs/perf-mon)
* [Google Analytics for Firebase](https://firebase.google.com/docs/analytics)
* [CircleCI](https://circleci.com)
* [SonarCloud](https://sonarcloud.io)
* [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics)
