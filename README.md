# WurmMapUploader
This is very basic command-line uploader of heightmap, terrain and cave map images to Wurm Unlimited map directory

Requires WurmModServerApi (https://github.com/codeclubab/WurmModServerApi)

#Installation

1. Copy WurmMapUploader.java file to WurmModServerApi source folder where WurmAPI.java is stored.

2. Optional: change settings in the WurmMapUploader.java source file (ELEVATION_SCALE, ELEVATION_SHIFT, IMPORT_CAVE_IMAGE, TWEAK_HEIGHT, REMOVE_EXCESSIVE_TREES). See below.

3. Compile with javac or run in IDE.

#Input files

Heightmap must be in 16 bit greyscale PNG format.

Terrain and cave maps must be in 24 or 32 bit color PNG format.

Only 2048x2048 images can be uploaded in the current version

#Available commands

java WurmMapUploader create [map folder]

Creates blank 2048x2084 Wurm Unlimited map in the folder specified. Other map sizes are not supported in the current version.

java WurmMapUploader preview [WU map folder] [height map image] [terrain map image] [cave map image]

Creates dump images for height map, terrain map and cave map images without uploading them. NOTE: these dumps for image files, NOT for map data in WU map folder. EXISTING DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.

java WurmMapUploader load [WU map folder] [height map image] [terrain map image] [cave map image]

Uploads height, terrain and cave maps AND creates dump images. EXISTING MAP DATA IS NOT PRESERVED AND DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.

java WurmMapUploader dump [WU map folder]

Generate dump images for Wurm Unlimited map data. EXISTING DUMP FILES WILL BE OVERWRITTEN.

java WurmMapUploader help

Displays help message

#Settings in source code

ELEVATION_SCALE: scales down height map if it has too tall mountains. You may need to change it, depending on output of your terrain generator.

ELEVATION_SHIFT: shifts up or down the whole terrain, if it is placed too deep under water or too high. You may need to change it, depending on output of your terrain generator.

IMPORT_CAVE_IMAGE: set to false if you have no cave map image and need to generate it. 
Use this option to get a cave map with dense and uniform distribution of ore veins under land tiles.

Note: even if you set it false, you still must provide any color PNG file of proper size as a cave map. 

TWEAK_HEIGHT: if set to true, maximum water depth is set to -25 for marsh and -100 for other terrain. Underwater clay tiles will be elevated to +1 height.

REMOVE_EXCESSIVE_TREES: if set to true, some trees will be eliminated from the terrain map and changed to grass. This option is useful if you fill some areas with solid color of a tree type and want to make trees a little more  scattered. Set % of remaining trees in treesDensity variable.

TWEAK_TERRAIN: some tweaks that were used for initial creation of Treasure Island terrain (see test image files). In 99% of cases, this option should not be set true.

#Ages of trees and bushes

All trees and bushes have age YOUNG_THREE. Grass on tree and bush tiles has growth stage MEDIUM.

#Depth of dirt layer

Height of rock layer is calculated from height of tile and type of terrain. 

On rock tiles, rock layer height is equal to land surface height.

On cliff tiles, rock layer is 1 unit lower than height.

On marsh tiles, rock layer is 25 units lower than height.

On other terrain tiles, depth of a dirt layer depends on elevation (distance from water level):

- deep water tiles (lower than -100 from sea level) have 30 dirts over rock.

- underwater tiles from -100 to -1 deep have rock layer at (surface depth)*1.1 - 20 

- tiles on land with elevation between 0 and 999 have rock layer at elevation*0.9 - 20, 
thus depth of dirt layer ranges from 20 at elevation 0 to 120 at elevation 999

- tiles with elevation between 1000 and 2999 have rock layer at elevation*0.9 + 80,
thus depth of dirt layer ranges from 20 at elevation 1000 to 220 at elevation 2999

- tiles with elevation 3000 and more have 20 dirt over rock layer 

#Dump files

dump_image.png

"classical" Wurm map dump (semi-3D terrain)

terrain_dump_image.png

flat map dump with all terrain types in different colors

Note: this dump may be used as input terrain files unless it contains bushes or trees (all bushes and trees have the same color on the dump which is not recognized by the uploader)

terrain_dump_image_nowater.png

flat map dump with all terrain types in different colors with water added

topographic_dump_image.png

flat map dump with all terrain types in different colors and with contour lines.  The difference in elevation between successive contour lines is 50 dirt

cave_dump_image.png

flat map dump with cave types in different colors

#Colors for terrain map

Some terrain types are not supported in the current version of the uploader (for example, roads)

|Terrain type |Name|Red|Green|Blue|
|---|---|---|---|---|
|TILE_BUSH_CAMELLIA|Camellia bush|41|58|12|
|TILE_BUSH_GRAPE|Grape bush|41|58|22|
|TILE_BUSH_LAVENDER|Lavender bush|41|58|32|
|TILE_BUSH_OLEANDER|Oleander bush|41|58|42|
|TILE_BUSH_ROSE|Rose bush|41|58|52|
|TILE_BUSH_THORN|Thorn bush|41|58|62|
|TILE_CLAY|Clay|113|124|118|
|TILE_CLIFF|Cliff|155|151|148|
|TILE_DIRT|Dirt|75|63|47|
|TILE_GRASS|Grass|54|101|3|
|TILE_KELP|Kelp|54|101|23|
|TILE_LAVA|Lava|215|51|30|
|TILE_MARSH|Marsh|43|101|72|
|TILE_MOSS|Moss|106|142|56|
|TILE_MYCELIUM|Mycelium|71|2|51|
|TILE_PEAT|Peat|54|39|32|
|TILE_REED|Reed|54|101|63|
|TILE_ROCK|Rock|114|110|107|
|TILE_SAND|Sand|160|147|109|
|TILE_SNOW|Snow|255|255|255|
|TILE_STEPPE|Steppe|114|117|67|
|TILE_TAR|Tar|18|21|40|
|TILE_TREE_APPLE|Apple tree|41|58|92|
|TILE_TREE_BIRCH|Birch tree|41|58|102|
|TILE_TREE_CEDAR|Cedar tree|41|58|112|
|TILE_TREE_CHERRY|Cherry tree|41|58|122|
|TILE_TREE_CHESTNUT|Chestnut tree|41|58|132|
|TILE_TREE_FIR|Fir tree|41|58|142|
|TILE_TREE_LEMON|Lemon tree|41|58|152|
|TILE_TREE_LINDEN|Linden tree|41|58|162|
|TILE_TREE_MAPLE|Maple tree|41|58|172|
|TILE_TREE_OAK|Oak tree|41|58|182|
|TILE_TREE_OLIVE|Olive tree|41|58|192|
|TILE_TREE_PINE|Pine tree|41|58|202|
|TILE_TREE_WALNUT|Walnut tree|41|58|212|
|TILE_TREE_WILLOW|Willow tree|41|58|222|
|TILE_TUNDRA|Tundra|118|135|109|

#Colors for cave map

WurmModServerApi only allows solid cave walls, so some cave tiles cannot be imported (for example, reinforced floors)

|Tile type|Name|Red|Green|Blue|
|---|---|---|---|---|
|TILE_CAVE_WALL|Cave wall|127|127|127|
|TILE_CAVE_WALL_LAVA|Lava wall|215|51|30|
|TILE_CAVE_WALL_MARBLE|Marble wall|238|238|238|
|TILE_CAVE_WALL_ORE_ADAMANTINE|Adamantine vein|41|58|122|
|TILE_CAVE_WALL_ORE_COPPER|Copper vein|62|116|107|
|TILE_CAVE_WALL_ORE_GLIMMERSTEEL|Glimmersteel vein|200|200|200|
|TILE_CAVE_WALL_ORE_GOLD|Gold vein|255|216|0|
|TILE_CAVE_WALL_ORE_IRON|Iron vein|46|32|21|
|TILE_CAVE_WALL_ORE_LEAD|Lead vein|85|84|78|
|TILE_CAVE_WALL_ORE_SILVER|Silver vein|165|165|165|
|TILE_CAVE_WALL_ORE_TIN|Tin vein|41|58|12|
|TILE_CAVE_WALL_ORE_ZINC|Zinc vein|104|125|134|
|TILE_CAVE_WALL_SLATE|Slate wall|30|30|30|
|TILE_CAVE_WALL_REINFORCED|Reinforced cave wall|64|64|64|

#Colors that appear in cave dump image but can not be used in cave map image

|Tile type|Name|Red|Green|Blue|
|---|---|---|---|---|
|TILE_CAVE|Cave|255|255|255|
|TILE_CAVE_EXIT|Cave|0|0|0|
|TILE_CAVE_FLOOR_REINFORCED|Reinforced cave|185|185|185|
||Non-cave tile|255|127|137|
||Unknown type of cave tile|255|0|0|
