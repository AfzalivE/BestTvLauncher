# Stream History

## February 5th 2022
- Use AnimatedContent for HomeScreen, improving performance while switch between Channels and Apps screens
- Switched to Compose Destinations library for cleaner Navigation code
- Added watch next retrieval to Android TV provider library

## February 12th 2022
- Fixed apps scaling when switching tabs
- Fixed thumbnail aspect ratio
- Fixed black underline on tabs

## February 19th 2022
- Add metadata and button rows in ItemDetails screen
- Add Channel to ItemDetails transition

# March 12th 2022
- Fix app needing restart after TV listings permission is granted
- Cache channel and program data in memory
- Update Coil to 2.0.0-rc01, significant different in load times! 🎉 

# March 19th 2022
- Fix crash when opening Item Details screen for some movies 🐛
- Use Material 2 TopAppBar instead of M3 because M3 doesn't support TabRow in title

# March 26th 2022
- Switch navigation to Voyager. Better tabs when content scrolls on the main axis (horizontally)
- Add colored shadows according to thumbnail's vibrant swatch using Palette

# August 6th 2022
- Switch navigation to Androidx and use AnimatedNavHost for transitions
- Add shimmer loading state to Channels screen
- Use TvLazyColumn/Row/Grid for Lazy components for better focus
- Move some loading off main thread

# August 13th 2022
- Refactor and remove unused code
- Combine channel & program, watch next, and app info loading using Flows
- Use Compose Metrics and Kotlin Immutable Collections to make Composables skippable

# August 27th 2022
- Adjusted TitleBar padding
- Adjusted card size and text width
- Added slide and fade transitions to tabs

# September 10th 2022
- Fixed stutter when switching to Apps screen
- Use a custom Coil fetcher to get app banners, making AppInfo skippable 🎉 

# September 25th 2022
- Tried out ImmersiveList, didn't work quite seamlessly
- Animated background color on focus using Palette

# November 13th 2022
- Created a basic macro-benchmark "test" 
- Tried out Baseline profiles but it seems impossible to generate one from a TV emulator
