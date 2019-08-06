import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Field implements GLEventListener, KeyListener {

    private final Sound SOUND_DIE = new Sound( "Data/Die.wav" );
    private final Sound SOUND_COMPLETE = new Sound( "Data/Complete.wav" );
    private final Sound SOUND_FREEZE = new Sound( "Data/Freeze.wav" );
    private final Sound SOUND_HOURGLASS = new Sound( "Data/Hourglass.wav" );

    private boolean vLine[][] = new boolean[ 11 ][ 11 ];
    private boolean hLine[][] = new boolean[ 11 ][ 11 ];
    private boolean filled;
    private boolean gameOver;
    private boolean resetGame;
    private boolean anti = true;
    private boolean isHourglass;
    private int delay;
    private int adjust = 3;
    private int lives = 5;
    private int level = 1;
    private int level2 = level;
    private int stage = 1;
    private int base;
    private int steps[] = new int[]{ 1, 2, 4, 5, 10, 20 };
    private GameObject player = new GameObject();
    private GameObject hourGlass = new GameObject();
    private GameObject[] enemy = new GameObject[ 9 ];
    private IntBuffer textureIDs = IntBuffer.wrap( new int[ 2 ] );
    private long lastUpdatedTime;

    {
        for (int i = 0; i < 9; i++) {

            enemy[ i ] = new GameObject();
        }
    }



    private void resetMyObjects() {

        player.x = 0;
        player.y = 0;
        player.fx = 0;
        player.fy = 0;
        for (int i = 0; i < stage * level; i++) {

            enemy[ i ].x = 5 + ( int )( Math.random() * 6 );
            enemy[ i ].y = ( int )( Math.random() * 11 );
            enemy[ i ].fx = enemy[ i ].x * 60;
            enemy[ i ].fy = enemy[ i ].y * 40;
        }
    }

    private void loadGLTextures( GL2 gl ) throws IOException {

        List<File> txFiles = new ArrayList<>();
        txFiles.add( new File( "Data/Font.png" ) );
        txFiles.add( new File( "Data/Image.png" ) );
        gl.glGenTextures( 2, textureIDs );
        for ( int i = 0; i < 2; i++ ) {

            Texture tx = TextureIO.newTexture( txFiles.get( i ), true );
            TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txFiles.get( i ), true, null );
            textureIDs.put( i, tx.getTextureObject() );
            gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( i ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, tx.getWidth(), tx.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        }
    }

    private void buildFont( GL2 gl ) {

        float cx, cy;
        base = gl.glGenLists( 256 );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 0 ) );
        for (int i = 0; i < 256; i++) {

            cx = ( float )( i % 16 ) / 16;
            cy = ( float )( i / 16 ) / 16;
            gl.glNewList( base + i, GL2.GL_COMPILE );
            gl.glBegin( GL2.GL_QUADS );
            gl.glTexCoord2f( cx, 1f - cy - 0.0625f );
            gl.glVertex2i( 0, 16 );
            gl.glTexCoord2f( cx + 0.0625f, 1f - cy - 0.0625f );
            gl.glVertex2i( 16, 16 );
            gl.glTexCoord2f( cx + 0.0625f, 1f - cy );
            gl.glVertex2i( 16, 0 );
            gl.glTexCoord2f( cx, 1f - cy );
            gl.glVertex2i( 0, 0 );
            gl.glEnd();
            gl.glTranslatef( 15f, 0, 0 );
            gl.glEndList();
        }
    }

    @Override
    public void init( GLAutoDrawable drawable ) {

        GL2 gl = drawable.getGL().getGL2();
        try {

            loadGLTextures( gl );
        } catch ( IOException e ) {

            e.printStackTrace();
        }
        buildFont( gl );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0.5f );
        gl.glClearDepth( 1f );
        gl.glHint( GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST );
        gl.glEnable( GL2.GL_BLEND );
        gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA );
        resetMyObjects();
    }

    @Override
    public void dispose( GLAutoDrawable drawable ) {


    }

    private void printText( GL2 gl, int x, int y, String text, int set ) {

        if( set > 1 ) {

            set = 1;
        }
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glLoadIdentity();
        gl.glTranslated( x, y, 0 );
        gl.glListBase( base - 32 + 128 * set );
        if( set == 0 ) {

            gl.glScalef( 1.5f, 2f, 1f );
        }
        ByteBuffer str = ByteBuffer.wrap( text.getBytes() );
        str.flip();
        gl.glCallLists( text.length(), GL2.GL_UNSIGNED_BYTE, str );
        gl.glDisable( GL2.GL_TEXTURE_2D );
    }

    private long getTime() {

        return System.currentTimeMillis();
    }

    @Override
    public void display( GLAutoDrawable drawable ) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 0 ) );
        gl.glColor3f( 1f, 0.5f, 1f );
        printText( gl, 207, 24, "GRID CRAZY", 0 );
        gl.glColor3f( 1f, 1f, 0f );
        printText( gl, 20, 20, String.format( "Level:%2d", level2 ), 1 );
        printText( gl, 20, 40, String.format( "Stage:%2d", stage ), 1 );
        if( gameOver ) {

            gl.glColor3f( ( float )Math.random(), ( float )Math.random(), ( float )Math.random() );
            printText( gl, 472, 20, "GAME OVER", 1 );
            printText( gl, 456, 40, "PRESS SPACE", 1 );
        }
        for ( int i = 0; i < lives - 1; i++ ) {

            gl.glLoadIdentity();
            gl.glTranslatef( 490f + i * 40f, 40f, 0f );
            gl.glRotatef( -player.spin, 0f, 0f, 1f );
            gl.glColor3f( 0f, 1f, 0f );
            gl.glBegin( GL2.GL_LINES );
            gl.glVertex2d( -5, -5 );
            gl.glVertex2d( 5, 5 );
            gl.glVertex2d( 5, -5 );
            gl.glVertex2d( -5, 5 );
            gl.glEnd();
            gl.glRotatef( -0.5f * player.spin, 0f, 0f, 1f );
            gl.glColor3f( 0f, 0.75f, 0f );
            gl.glBegin( GL2.GL_LINES );
            gl.glVertex2d( -7, 0 );
            gl.glVertex2d( 7, 0 );
            gl.glVertex2d( 0, -7 );
            gl.glVertex2d( 0, 7 );
            gl.glEnd();
        }
        filled = true;
        gl.glLineWidth( 2f );
        gl.glDisable( GL2.GL_LINE_SMOOTH );
        gl.glLoadIdentity();
        for ( int i = 0; i < 11; i++ ) {

            for ( int j = 0; j < 11; j++ ) {

                gl.glColor3f( 0f, 0.5f, 1f );
                if( hLine[ i ][ j ] ) {

                    gl.glColor3f( 1f, 1f, 1f );
                }
                if( i < 10 ) {

                    if( !hLine[ i ][ j ] ) {

                        filled = false;
                    }
                    gl.glBegin( GL2.GL_LINES );
                     gl.glVertex2d( 20 + i * 60, 70 + j * 40 );
                     gl.glVertex2d( 80 + i * 60, 70 + j * 40 );
                    gl.glEnd();
                }
                gl.glColor3f( 0f, 0.5f, 1f );
                if( vLine[ i ][ j ] ) {

                    gl.glColor3f( 1f, 1f, 1f );
                }
                if( j < 10 ) {

                    if( !vLine[ i ][ j ] ) {

                        filled = false;
                    }
                    gl.glBegin( GL2.GL_LINES );
                    gl.glVertex2d( 20 + i * 60, 70 + j * 40 );
                    gl.glVertex2d( 20 + i * 60, 110 + j * 40 );
                    gl.glEnd();
                }
                gl.glEnable( GL2.GL_TEXTURE_2D );
                gl.glColor3f( 1f, 1f, 1f );
                gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 1 ) );
                if ( i < 10 && j < 10 ) {

                    if( ( hLine[ i ][ j ] && hLine[ i ][ j + 1 ] ) && ( vLine[ i ][ j ] && vLine[ i + 1 ][ j ] ) ) {

                        gl.glBegin( GL2.GL_QUADS );
                        gl.glTexCoord2f( ( float ) i / 10f + 0.1f, 1f - ( float ) j / 10f ); //Право верх
                        gl.glVertex2d( 20 + i * 60 + 59, 70 + j * 40 + 1 );
                        gl.glTexCoord2f( ( float ) i / 10f, 1f - ( float ) j / 10f ); //Лево верх
                        gl.glVertex2d( 20 + i * 60 + 1, 70 + j * 40 + 1 );
                        gl.glTexCoord2f( ( float ) i / 10f, 1f - ( float ) j / 10f - 0.1f ); //Лево низ
                        gl.glVertex2d( 20 + i * 60 + 1, 70 + j * 40 + 39 );
                        gl.glTexCoord2f( ( float ) i / 10f + 0.1f, 1f - ( float ) j / 10f - 0.1f ); //Право низ
                        gl.glVertex2d( 20 + i * 60 + 59, 70 + j * 40 + 39 );
                        gl.glEnd();
                    }
                }
                gl.glDisable( GL2.GL_TEXTURE_2D );
            }
        }
        gl.glLineWidth( 1f );
        if( anti ) {

            gl.glEnable( GL2.GL_SMOOTH );
        }
        if( hourGlass.fx == 1 ) {

            gl.glLoadIdentity();
            gl.glTranslatef( 20f + ( float ) hourGlass.x * 60, 70f + ( float ) hourGlass.y * 40f, 0f );
            gl.glRotatef( hourGlass.spin, 0f, 0f, 1f );
            gl.glColor3f( ( float )Math.random(), ( float )Math.random(), ( float )Math.random() );
            gl.glBegin( GL2.GL_LINES );
            gl.glVertex2d( -5, -5 );
            gl.glVertex2d( 5, 5 );
            gl.glVertex2d( 5, -5 );
            gl.glVertex2d( -5, 5 );
            gl.glVertex2d( -5, 5 );
            gl.glVertex2d( 5, 5 );
            gl.glVertex2d( -5, -5 );
            gl.glVertex2d( 5, -5 );
            gl.glEnd();
        }
        gl.glLoadIdentity();
        gl.glTranslatef( player.fx + 20f, player.fy + 70f, 0f );
        gl.glRotatef( player.spin, 0f, 0f, 1f );
        gl.glColor3f( 0f, 1f, 0f );
        gl.glBegin( GL2.GL_LINES );
        gl.glVertex2d( -5, -5 );
        gl.glVertex2d( 5, 5 );
        gl.glVertex2d( 5, -5 );
        gl.glVertex2d( -5, 5 );
        gl.glEnd();
        gl.glRotatef( player.spin * 0.5f, 0f, 0f, 1f );
        gl.glColor3f( 0f, 0.75f, 0f );
        gl.glBegin( GL2.GL_LINES );
        gl.glVertex2d( -7, 0 );
        gl.glVertex2d( 7, 0 );
        gl.glVertex2d( 0, -7 );
        gl.glVertex2d( 0, 7 );
        gl.glEnd();
        for (int i = 0; i < stage * level; i++) {

            gl.glLoadIdentity();
            gl.glTranslatef( enemy[ i ].fx + 20f, enemy[ i ].fy + 70f, 0f );
            gl.glColor3f( 1f, 0.5f, 0.5f );
            gl.glBegin( GL2.GL_LINES );
            gl.glVertex2d( 0, -7 );
            gl.glVertex2d( -7, 0 );
            gl.glVertex2d( -7, 0 );
            gl.glVertex2d( 0, 7 );
            gl.glVertex2d( 0, 7 );
            gl.glVertex2d( 7, 0 );
            gl.glVertex2d( 7, 0 );
            gl.glVertex2d( 0, -7 );
            gl.glEnd();
            gl.glRotatef( enemy[ i ].spin, 0f, 0f, 1f );
            gl.glColor3f( 1f, 0f, 0f );
            gl.glBegin( GL2.GL_LINES );
            gl.glVertex2d( -7, -7 );
            gl.glVertex2d( -7, -7 );
            gl.glVertex2d( -7, 7 );
            gl.glVertex2d( 7, -7 );
            gl.glEnd();
        }
        long currentTime = getTime();
        long elapsedTime = currentTime - lastUpdatedTime;
        if( elapsedTime >  steps[ adjust ] * 2 ) {

            lastUpdatedTime = getTime();
            if( !gameOver  ) {

                for (int i = 0; i < stage * level; i++) {

                    if( enemy[ i ].fy == enemy[ i ].y * 40 ) {

                        if( enemy[ i ].x < player.x ) {

                            enemy[ i ].x++;
                        } else if( enemy[ i ].x > player.x ) {

                            enemy[ i ].x--;
                        }
                    }
                    if( enemy[ i ].fx == enemy[ i ].x * 60 ) {

                        if( enemy[ i ].y < player.y ) {

                            enemy[ i ].y++;
                        } else if( enemy[ i ].y > player.y ) {

                            enemy[ i ].y--;
                        }
                    }
                    if( delay > ( 3 - level ) && hourGlass.fx != 2 ) {

                        delay = 0;
                        for (int j = 0; j < stage * level; j++) {

                            if( enemy[ j ].fx < enemy[ j ].x * 60 ) {

                                enemy[ j ].fx += steps[ adjust ];
                                enemy[ j ].spin += steps[ adjust ];
                            }
                            if( enemy[ j ].fx > enemy[ j ].x * 60 ) {

                                enemy[ j ].fx -= steps[ adjust ];
                                enemy[ j ].spin -= steps[ adjust ];
                            }
                            if( enemy[ j ].fy < enemy[ j ].y * 40 ) {

                                enemy[ j ].fy += steps[ adjust ];
                                enemy[ j ].spin += steps[ adjust ];
                            }
                            if( enemy[ j ].fy > enemy[ j ].y * 40 ) {

                                enemy[ j ].fy -= steps[ adjust ];
                                enemy[ j ].spin -= steps[ adjust ];
                            }
                        }
                    }
                    if( enemy[ i ].fx == player.fx && enemy[ i ].fy == player.fy ) {

                        lives--;
                        if( lives == 0 ) {

                            gameOver = true;
                        }
                        resetMyObjects();
                        SOUND_DIE.play().join();
                    }
                }
                if( player.fx < player.x * 60 ) {

                    player.fx += steps[ adjust ];
                }
                if( player.fx > player.x * 60 ) {

                    player.fx -= steps[ adjust ];
                }
                if ( player.fy > player.y * 40 ) {

                    player.fy -= steps[ adjust ];
                }
                if( player.fy < player.y * 40 ) {

                    player.fy += steps[ adjust ];
                }
            }
            if( filled || resetGame ) {

                SOUND_COMPLETE.play().join();
                stage++;
                if( stage > 3 ) {

                    stage = 1;
                    level++;
                    level2++;
                    if( level > 3) {

                        level = 3;
                        lives++;
                        if( lives > 5 ) {

                            lives = 5;
                        }
                    }
                }
                resetMyObjects();
                for (int i = 0; i < 11; i++) {

                    for (int j = 0; j < 11; j++) {

                        if( i < 10 ) {

                            hLine[ i ][ j ] = false;
                        }
                        if( j < 10 ) {

                            vLine[ i ][ j ] = false;
                        }
                    }
                }
                if( resetGame ) {

                    resetGame = false;
                }
            }
            if( player.fx == hourGlass.x * 60 && player.fy == hourGlass.y * 40 && hourGlass.fx == 1 ) {

                SOUND_FREEZE.play( true );
                hourGlass.fx = 2;
                hourGlass.fy = 0;
            }
            player.spin += 0.5f * ( float ) steps[ adjust ];
            if( player.spin > 360f ) {

                player.spin -= 360f;
            }
            hourGlass.spin -= 0.25f * ( float ) steps[ adjust ];
            if( hourGlass.spin < 0f ) {

                hourGlass.spin += 360f;
            }
            hourGlass.fy += steps[ adjust ];
            if( hourGlass.fx == 0 && hourGlass.fy > 6000 / level ) {

                SOUND_HOURGLASS.play();
                hourGlass.x = ( int ) ( Math.random() * 10 ) + 1;
                hourGlass.y = ( int ) ( Math.random() * 11 );
                hourGlass.fx = 1;
                hourGlass.fy = 0;
            }
            if( hourGlass.fx == 1 && hourGlass.fy > 6000 / level ) {

                hourGlass.fx = 0;
                hourGlass.fy = 0;
            }
            if( hourGlass.fx == 2 && hourGlass.fy > 500 + 500 * level ) {

                SOUND_FREEZE.stop();
                hourGlass.fx = 0;
                hourGlass.fy = 0;
            }
            delay++;
        }
    }

    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        if( height == 0 ) {

            height = 1;
        }
        gl.glOrthof( 0f, width, height, 0f, -1f, 1f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    private static class GameObject {

        int x, y;
        int fx, fy;
        float spin;

        GameObject(){}

        GameObject(int x, int y, int fx, int fy) {

            this.x = x;
            this.y = y;
            this.fx = fx;
            this.fy = fy;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch ( e.getKeyCode() ) {

            case KeyEvent.VK_A: {

                anti = !anti;
                break;
            }
            case KeyEvent.VK_RIGHT: {

                if( player.x < 10 && player.fx == player.x * 60 && player.fy == player.y * 40 ) {

                    hLine[ player.x ][ player.y ] = true;
                    player.x++;
                }
                break;
            }
            case KeyEvent.VK_LEFT: {

                if( player.x > 0 && player.fx == player.x * 60 && player.fy == player.y * 40 ) {

                    player.x--;
                    hLine[ player.x ][ player.y ] = true;
                }
                break;
            }
            case KeyEvent.VK_DOWN: {

                if( player.y < 10 && player.fx == player.x * 60 && player.fy == player.y * 40 ) {

                    vLine[ player.x ][ player.y ] = true;
                    player.y++;
                }
                break;
            }
            case KeyEvent.VK_UP: {

                if( player.y > 0 && player.fx == player.x * 60 && player.fy == player.y * 40 ) {

                    player.y--;
                    vLine[ player.x ][ player.y ] = true;
                }
                break;
            }
            case KeyEvent.VK_SPACE: {

                if( gameOver ) {

                    gameOver = false;
                    resetGame = true;
                    level = 1;
                    level2 = 1;
                    stage = 0;
                    lives = 5;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
