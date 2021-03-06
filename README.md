# WurmMapUploader
This is very basic command-line uploader of heightmap, terrain and cave map images to Wurm Unlimited map directory

Requires WurmModServerApi (https://github.com/codeclubab/WurmModServerApi)

#Installation

1. Copy WurmMapUploader.java file to WurmModServerApi source folder where WurmAPI.java is stored.

2. Optional: change settings in the WurmMapUploader.java source file (see below).

3. Compile with javac or run in IDE.

#Input files

Surface and rock layer height maps must be in 16 bit greyscale PNG format.

Terrain and cave maps must be in 24 or 32 bit color PNG format.

Only 1024x1024, 2048x2048 or 4096x4096 images can be uploaded

#Available commands

java WurmMapUploader create [map folder] [1024|2048|4096]

Creates blank Wurm Unlimited map in the folder specified. Map sizes may be 1024, 2048 or 4096. Larger map sizes are not supported.

java WurmMapUploader preview [WU map folder] [height map image] [terrain map image] [cave map image] [rock height map image]

Creates dump images for height map, terrain map, cave map and rock height map images without uploading them. NOTE: these dumps for image files, NOT for map data in WU map folder. EXISTING DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.

java WurmMapUploader load [WU map folder] [height map image] [terrain map image] [cave map image] [rock height map image]

Uploads height, terrain, cave and rock height maps AND creates dump images. EXISTING MAP DATA IS NOT PRESERVED AND DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.

java WurmMapUploader dump [WU map folder]

Generates dump images for Wurm Unlimited map data. EXISTING DUMP FILES WILL BE OVERWRITTEN.
        
java WurmMapUploader export [WU map folder]

Generates height maps of surface and rock layer as 16 bit grayscale PNG files. Generates terrain map and cave map as color PNG files. EXISTING EXPORTED FILES WILL BE OVERWRITTEN.

Important note: information about flower types is not exported. All grass tiles are exported as FlowerType.NONE.

java WurmMapUploader help

Displays help message

#Settings in source code

RANDOMIZE_TREE_AGE: generates trees with random age. If set to false, all trees will have age YOUNG_THREE

RANDOMIZE_BUSH_AGE: generates bushes with random age. If set to false, all bushes will have age YOUNG_THREE

RANDOMIZE_GRASS_GROWTH_STAGE: generates grass tiles with random growth stage. If set to false, all grass tiles will have growth stage MEDIUM

RANDOMIZE_FLOWER_TYPES: if true, random flower types will be set on grass tiles. If set to false, flower types will be set according to color codes in terrain map.

Flower types data may not be exported and not shown on dumps. DO NOT OVERWRITE your terrain map with flower types data, it cannot be recovered from the exported terrain map.

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

flat map dump with cave types in different colors with water added

#Test example

1. Create a new blank map in map-test directory:

java WurmMapUploader create map-test

2. Load test data and create dump files:

java WurmMapUploader load ".\map-test" "test-data\heightmap.png" "test-data\terrain.png" "test-data\caves.png" "test-data\rock.png"

Note: if you start WurmMapUploader.java from other directory, change path to test-data directory.

#Colors for terrain map

Some terrain types are not supported by the uploader (for example, roads)

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
|TILE_GRASS with FlowerType.NONE|Grass|249|249|169|
|TILE_GRASS with FlowerType.FLOWER_1|Grass|249|249|174|
|TILE_GRASS with FlowerType.FLOWER_2|Grass|249|249|179|
|TILE_GRASS with FlowerType.FLOWER_3|Grass|249|249|184|
|TILE_GRASS with FlowerType.FLOWER_4|Grass|249|249|189|
|TILE_GRASS with FlowerType.FLOWER_5|Grass|249|249|194|
|TILE_GRASS with FlowerType.FLOWER_6|Grass|249|249|199|
|TILE_GRASS with FlowerType.FLOWER_7|Grass|249|249|204|
|TILE_GRASS with FlowerType.FLOWER_8|Grass|249|249|209|
|TILE_GRASS with FlowerType.FLOWER_9|Grass|249|249|214|
|TILE_GRASS with FlowerType.FLOWER_10|Grass|249|249|219|
|TILE_GRASS with FlowerType.FLOWER_11|Grass|249|249|224|
|TILE_GRASS with FlowerType.FLOWER_12|Grass|249|249|229|
|TILE_GRASS with FlowerType.FLOWER_13|Grass|249|249|234|
|TILE_GRASS with FlowerType.FLOWER_14|Grass|249|249|239|
|TILE_GRASS with FlowerType.FLOWER_15|Grass|249|249|244|
|TILE_KELP|Kelp|99|181|41|
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

