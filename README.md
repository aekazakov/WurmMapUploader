# WurmMapUploader
This is very basic command-line uploader of heightmap, terrain and cave map images to Wurm Unlimited map directory

Requires WurmModServerApi (https://github.com/codeclubab/WurmModServerApi)

#Installation

1. Copy WurmMapUploader.java file to WurmModServerApi source folder where WurmAPI.java is stored.

2. Optional: change settings in the WurmMapUploader.java source file (ELEVATION_SCALE, ELEVATION_SHIFT, IMPORT_CAVE_IMAGE, TWEAK_HEIGHT, REMOVE_EXCESSIVE_TREES). See below.

3. Compile with javac or run in IDE.

#Input files

Surface and rock layer height maps must be in 16 bit greyscale PNG format.

Terrain and cave maps must be in 24 or 32 bit color PNG format.

Only 2048x2048 images can be uploaded in the current version

#Available commands

java WurmMapUploader create [map folder]

Creates blank 2048x2084 Wurm Unlimited map in the folder specified. Other map sizes are not supported in the current version.

java WurmMapUploader preview [WU map folder] [height map image] [terrain map image] [cave map image] [rock height map image]

Creates dump images for height map, terrain map, cave map and rock height map images without uploading them. NOTE: these dumps for image files, NOT for map data in WU map folder. EXISTING DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.

java WurmMapUploader load [WU map folder] [height map image] [terrain map image] [cave map image] [rock height map image]

Uploads height, terrain, cave and rock height maps AND creates dump images. EXISTING MAP DATA IS NOT PRESERVED AND DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.

java WurmMapUploader dump [WU map folder]

Generates dump images for Wurm Unlimited map data. EXISTING DUMP FILES WILL BE OVERWRITTEN.
        
java WurmMapUploader export [WU map folder]

Generates height maps surface and rock layer as 16 bit grayscale PNG files. EXISTING EXPORTED FILES WILL BE OVERWRITTEN.

java WurmMapUploader help

Displays help message

#Settings in source code

RANDOMIZE_TREE_AGE: generates trees with random age. If set to false, all trees will have age YOUNG_THREE

RANDOMIZE_BUSH_AGE: generates bushes with random age. If set to false, all bushes will have age YOUNG_THREE

RANDOMIZE_GRASS_GROWTH_STAGE: generates grass tiles with random growth stage. If set to false, all grass tiles will have growth stage MEDIUM

flowersDensity: sets % of grass tiles with flowers 

ELEVATION_SCALE: scales down height map if it has too tall mountains. You may need to change it, depending on output of your terrain generator.

ELEVATION_SHIFT: shifts up or down the whole terrain, if it is placed too deep under water or too high. You may need to change it, depending on output of your terrain generator.

ROCK_HEIGHT_SHIFT and ROCK_HEIGHT_SCALE affect the rock layer in the same way

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

#Test example

1. Create a new blank map in map-test directory:

java WurmMapUploader create map-test

2. Load test data and create dump files:

java WurmMapUploader load ".\map-test" "test-data\heightmap.png" "test-data\terrain.png" "test-data\caves.png" "test-data\rock.png"

Note: if you start WurmMapUploader.java from other directory, change path to test-data directory.

#Colors for terrain map

Some terrain types are not supported in the current version of the uploader (for example, roads)

|Terrain type |Name|Red|Green|Blue|
|---|---|---|---|---|
|TILE_BUSH_CAMELLIA|Camellia bush|249|249|159|
|TILE_BUSH_GRAPE|Grape bush|153|62|155|
|TILE_BUSH_LAVENDER|Lavender bush|214|66|255|
|TILE_BUSH_OLEANDER|Oleander bush|249|197|238|
|TILE_BUSH_ROSE|Rose bush|255|43|50|
|TILE_BUSH_THORN|Thorn bush|136|50|44|
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
|TILE_TREE_APPLE|Apple tree|255|190|0|
|TILE_TREE_BIRCH|Birch tree|247|229|64|
|TILE_TREE_CEDAR|Cedar tree|78|221|46|
|TILE_TREE_CHERRY|Cherry tree|226|34|0|
|TILE_TREE_CHESTNUT|Chestnut tree|0|211|98|
|TILE_TREE_FIR|Fir tree|90|206|146|
|TILE_TREE_LEMON|Lemon tree|255|233|94|
|TILE_TREE_LINDEN|Linden tree|171|193|89|
|TILE_TREE_MAPLE|Maple tree|183|60|0|
|TILE_TREE_OAK|Oak tree|123|198|1|
|TILE_TREE_OLIVE|Olive tree|144|160|110|
|TILE_TREE_PINE|Pine tree|186|75|24|
|TILE_TREE_WALNUT|Walnut tree|128|219|17|
|TILE_TREE_WILLOW|Willow tree|121|193|108|
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
