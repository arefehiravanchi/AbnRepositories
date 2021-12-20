# Abn Repositories
## Overview

***ABN REPOSITORIES*** is a simple application, which shows a list of ABN Ambro Github repositories with some of the repository and owner's information. It also shows the detail of each repository if the user clicks on an item.
The app supports offline mode which usually provides a good user experience even if the network is slow unavailable.

## Technical Overview
The app is developed upon MVVM architecture and uses 2 data sources:
Network
Cache / Local Database
To support offline mode, the app uses the database as a single source of truth. So the data will be cached in the local database after each API call.
The cache data will be updated if the network connection is available.

## Tools
- Combination of Retrofit and Gson to fetch network data
- Coroutine and Flow to handle async tasks
- Paging3 to support pagination
- Room database to cache data
- Hilt to implement dependency injection
- Livedata to update UI
- Navigation component to handle fragments

The logic implementations to fetch data from the network and cache (Repositories, RemoteMediator, ViewModel, Database) are all test covered.