package doomies.Visual;

import doomies.HerramientasEntradaSalida.LoadTools;
import java.awt.image.BufferedImage;

/**
 * Clase que representa la hoja de sprites, su funcionalidad principal es
 * generar los sprites a partir de las hojas de sprites
 *
 * @author Víctor
 * @version 4
 * @since 1
 */
public class SpriteSheet {

    /**
     * Anchura de la hoja de sprites en px
     */
    private final int WIDTH;
    /**
     * Altura de la hoja de sprites en px
     */
    private final int HEIGHT;
    /**
     * Anchura de los sprites
     */
    private final int SPRITE_WIDTH;
    /**
     * Altura de los sprites
     */
    private final int SPRITE_HEIGHT;
    /**
     * Imagen que representa la hoja
     */
    private final BufferedImage img;
    /**
     * Array de sprites que contiene la hoja
     */
    private final Sprite[][] sprites;
    /**
     * Coleccion de Hojas de sprites
     */
    public static final SpriteSheet PERSONAJE = new SpriteSheet(160, 636, 80, 106, "/spritesSheet/Character.png");
    public static SpriteSheet MAPA = new SpriteSheet(512, 512, 64, 64, "/spritesSheet/Mapa.png");
    public static SpriteSheet IMP = new SpriteSheet(183, 300, 91, 100, "/spritesSheet/Imp.png");
    public static SpriteSheet PINKIE = new SpriteSheet(168, 336, 84, "/spritesSheet/Pinkie.png");
    public static SpriteSheet CACODEMON = new SpriteSheet(214, 387, 107, 96, "/spritesSheet/Cacodemon.png");
    public static SpriteSheet SOUL = new SpriteSheet(124, 60, 62, 60, "/spritesSheet/Soul.png");
    public static SpriteSheet BARON = new SpriteSheet(258, 768, 129, 192, "/spritesSheet/Baron.png");

    /**
     * Crea la hoja de sprite, siendo sprites de un ancho y un alto diferente
     *
     * @param width int que representa la anchura de la hoja (px)
     * @param height int que representa la altura de la hoja (px)
     * @param sprite_width int que representa la anchura del sprite (px)
     * @param sprite_height int que representa la altura del sprite (px)
     * @param path String que representa la ruta de la hoja de sprites
     */
    public SpriteSheet(final int width, final int height, int sprite_width, final int sprite_height, final String path) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.SPRITE_WIDTH = sprite_width;
        this.SPRITE_HEIGHT = sprite_height;
        this.img = LoadTools.loadImage(path);
        sprites = new Sprite[HEIGHT / SPRITE_HEIGHT][WIDTH / SPRITE_WIDTH];//Array de Sprites que contienen toda la hoja
        getAllSprites();
    }

    /**
     * Crea la hoja de sprite, siendo sprites cuadrados
     *
     * @param width int que representa la anchura de la hoja (px)
     * @param height int que representa la altura de la hoja (px)
     * @param size int que representa la el tamaño del sprite (alto y ancho)
     * @param path String que indica la ruta donde se encuentra la imagen que
     * representa la hoja de sprites
     */
    public SpriteSheet(final int width, final int height, final int size, final String path) {
        this(width, height, size, size, path);
    }

    /**
     * Obtengo todos los sprites de la hoja de sprites teniendo en cuenta el
     * tamaño de los sprites
     */
    private void getAllSprites() {//Recorre la hoja de sprites entera y coje todos los sprites
        for (int i = 0; i < HEIGHT / SPRITE_HEIGHT; i++) {
            for (int j = 0; j < WIDTH / SPRITE_WIDTH; j++) {
                this.sprites[i][j] = new Sprite(img.getSubimage(j * SPRITE_WIDTH, i * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT));
            }
        }
    }

    /**
     *
     * @return Todos los sprites de la hoja de sprites
     */
    public Sprite[][] getSprites() {
        return sprites;
    }

    /**
     * Obtiene un sprite en especifico de la hoja de sprites
     *
     * @param x int representa que posicion (en sprites) es 0-n
     * @param y int representa que posicion (en sprites) es 0-n
     * @return Devuelve el sprite deseado
     */
    public Sprite getSprite(final int x, final int y) {

        return sprites[x][y];
    }
}
