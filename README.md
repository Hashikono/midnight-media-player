# midnight-media-player
A customized media player to be able to play media on your computer locally.
(currently only has DB manipulation though. Playing media is yet to be implemented.)


To run the program you need to go into the the App.Java file and run the main function on the bottom by pressing the button that appears over it.


For anyone that may want to tinker with the project, or even just the peeps I'm working with, here's the list af all the useful files and what they do:
- App: runs all the basic functions and where everything else stems from
- Database: runs the database
- ColorScheme: where all the components grab their colors from
- MediaControlBar: the bar at the bottom of the window that holds all the music controls.
- MusicPlayer: pretty much just threw a lot of the old functions into here, currently has no integration though
- NavBar: the bar on the left of the screen that holds the buttons to navigate the menus
- NavButton: the buttons for said nav bar
- SongMenu: Stores all the song items and spawns them
- SongItem: grabs all the data for a song given by the song menu component
- ImageUtils: random assortment of useful image related functions (currently 1)
- MediaAddingMenu: pop up menu for adding media to the DB
- PlaylistsMenu: shows all the playlists you have
- PlaylistItem: Displays a playlist to select
- PlaylistAddingMenu: pop up menu for creating playlists


// Lil Reminder for myself, the tool to read the db is the SQLite browser