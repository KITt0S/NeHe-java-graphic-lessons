import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class Field implements GLEventListener, KeyListener {

    private final int MAX_PARTICLES =  1000;

    private int textureID;
    private int loop;
    private int color;
    private int delay;
    private float slowdown = 2f;
    private float xSpeed;
    private float ySpeed;
    private float zoom = -40f;
    private boolean rainbow;
    private GLU glu;
    private int keyCode;
    private float[][] colors = new float[][]{

            { 1.0f, 0.5f, 0.5f }, { 1.0f, 0.75f, 0.5f }, { 1.0f, 1.0f, 0.5f }, { 0.75f, 1.0f, 0.5f },
            { 0.5f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.75f }, { 0.5f, 1.0f, 1.0f }, { 0.5f, 0.75f, 1.0f },
            { 0.5f, 0.5f, 1.0f }, { 0.75f, 0.5f, 1.0f }, { 1.0f, 0.5f, 1.0f }, { 1.0f, 0.5f, 0.75f }
    };
    private Particle[] particles  = new Particle[ MAX_PARTICLES ];

    private void loadGLTexture( GL2 gl ) throws IOException {

        File txFile = new File( "Data/Particle.png" );
        Texture texture = TextureIO.newTexture( txFile, true );
        textureID = texture.getTextureObject();
        TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txFile, true, null );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureID );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
        gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0,
                GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        try {

            loadGLTexture( gl );
        } catch ( IOException e ) {

            e.printStackTrace();
        }
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureID );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearDepth( 1f );
        gl.glDisable( GL2.GL_DEPTH_TEST );
        gl.glEnable( GL2.GL_BLEND );
        gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glHint( GL2.GL_POINT_SMOOTH_HINT, GL2.GL_NICEST );
        glu = GLU.createGLU( gl );
        for (int i = 0; i < MAX_PARTICLES; i++) {

            float life = 1f;
            float fade =  ( float ) ( Math.random() * 100 ) / 1000f + 0.003f;
            float r = colors[ i * 12 / MAX_PARTICLES ][ 0 ];
            float g = colors[ i * 12 / MAX_PARTICLES ][ 1 ];
            float b = colors[ i * 12 / MAX_PARTICLES ][ 2 ];
            float xi = ( float )  ( Math.random() * 50  - 26f ) * 10f;
            float yi = ( float )  ( Math.random() * 50  - 25f ) * 10f;
            float zi = ( float )  ( Math.random() * 50  - 25f ) * 10f;
            float xg = 0f;
            float yg = -0.8f;
            float zg = 0f;
            particles[ i ] = new Particle(true, life, fade, r, g, b, xi, yi, zi, xg, yg, zg );
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        Particle particle;
        for (int i = 0; i < MAX_PARTICLES; i++) {

            particle = particles[ i ];

            if( particle.isActive() ) {

                float x = particle.getX();
                float y = particle.getY();
                float z = particle.getZ() + zoom;
                gl.glColor4f( particle.getR(), particle.getG(), particle.getB(), particle.getLife() );
                gl.glBegin( GL2.GL_TRIANGLE_STRIP );
                gl.glTexCoord2f( 1f, 1f );
                gl.glVertex3f( x + 0.5f, y + 0.5f, z );
                gl.glTexCoord2f( 0f, 1f );
                gl.glVertex3f( x - 0.5f, y + 0.5f , z );
                gl.glTexCoord2f( 1f, 0f );
                gl.glVertex3f( x + 0.5f, y - 0.5f, z );
                gl.glTexCoord2f( 0f, 0f );
                gl.glVertex3f( x - 0.5f, y - 0.5f, z );
                gl.glEnd();
                particle.setX( particle.getX() + particle.getXi() / ( slowdown * 1000 ) );
                particle.setY( particle.getY() + particle.getYi() / ( slowdown * 1000 ) );
                particle.setZ( particle.getZ() + particle.getZi() / ( slowdown * 1000 ) );
                particle.setXi( particle.getXi() + particle.getXg() );
                particle.setYi( particle.getYi() + particle.getYg() );
                particle.setZi( particle.getZi() + particle.getZg() );
                particle.setLife( particle.getLife() - particle.getFade() );
                if( particle.getLife() < 0f ) {

                    particle.setLife( 1f );
                    particle.setFade(  ( float ) ( Math.random() * 100 ) / 1000f + 0.003f );
                    particle.setX( 0f );
                    particle.setY( 0f );
                    particle.setZ( 0f );
                    particle.setXi( xSpeed + ( float ) ( Math.random() * 60 ) - 32f );
                    particle.setYi( ySpeed + ( float ) ( Math.random() * 60 ) - 30f );
                    particle.setZi( ( float ) ( Math.random() * 60 ) - 30f );
                    particle.setR( colors[ color ][ 0 ] );
                    particle.setG( colors[ color ][ 1 ] );
                    particle.setB( colors[ color ][ 2 ] );
                }
                particles[ i ] = particle;
            }
        }
        if( rainbow && delay > 25 ) {

            delay = 0;
            color++;
            if( color > 11 ) {

                color = 0;
            }
        }
        delay++;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        if( height == 0 ) {

            height = 1;
        }
        float h = ( float ) width / ( float ) height;
        glu.gluPerspective( 45f, h, 0.1f, 200f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed( KeyEvent e ) {

        keyCode = e.getKeyCode();
        switch ( e.getKeyCode() ) {

            case KeyEvent.VK_ADD: {

                if( slowdown > 1f ) {

                    slowdown -= 0.01f;
                }
                break;
            }

            case KeyEvent.VK_SUBTRACT: {

                if( slowdown < 4f ) {

                    slowdown += 0.01f;
                }
                break;
            }

            case KeyEvent.VK_PAGE_UP: {

                zoom += 0.1f;
                break;
            }

            case KeyEvent.VK_PAGE_DOWN: {

                zoom -= 0.1f;
                break;
            }

            case KeyEvent.VK_ENTER: {

                rainbow = !rainbow;
                break;
            }

            case KeyEvent.VK_SPACE: {

                rainbow = false;
                delay = 0;
                color++;
                if( color > 11 ) {

                    color = 0;
                }
                break;
            }

            case KeyEvent.VK_UP: {

                if( ySpeed < 200 ) {

                    ySpeed += 0.1f;
                }
                break;
            }

            case KeyEvent.VK_DOWN: {

                if( ySpeed > -200 ) {

                    ySpeed -= 0.1f;
                }
                break;
            }

            case KeyEvent.VK_RIGHT: {

                if( xSpeed < 200 ) {

                    xSpeed += 0.1f;
                }
                break;
            }

            case KeyEvent.VK_LEFT: {

                if( xSpeed > -200 ) {

                    xSpeed -= 0.1f;
                }
                break;
            }

            case KeyEvent.VK_NUMPAD8: {

                for ( Particle particle :
                     particles ) {

                    if( particle.getYg() < 2.5f ) {

                        particle.setYg( particle.getYg() + 0.01f );
                    }
                }
                break;
            }

            case KeyEvent.VK_NUMPAD2: {

                for ( Particle particle :
                     particles ) {

                    if( particle.getYg() > -2.5f ) {

                        particle.setYg( particle.getYg() - 0.01f );
                    }
                }
                break;
            }

            case KeyEvent.VK_NUMPAD6: {

                for ( Particle particle :
                     particles ) {

                    if( particle.getXg()  < 2.5f ) {

                        particle.setXg( particle.getXg() + 0.01f );
                    }
                }
                break;
            }

            case KeyEvent.VK_NUMPAD4: {

                for ( Particle particle :
                     particles ) {

                    if( particle.getXg() > -2.5f ) {

                        particle.setXg( particle.getXg() - 0.01f );
                    }
                }
                break;
            }

            case KeyEvent.VK_TAB: {

                for ( Particle particle :
                     particles ) {

                    particle.setX( 0f );
                    particle.setY( 0f );
                    particle.setZ( 0f );
                    particle.setXi( ( ( float ) ( Math.random() * 50 % 50 ) - 26f ) * 10f );
                    particle.setYi( ( ( float ) ( Math.random() * 50 % 50 ) - 25f ) * 10f );
                    particle.setZi( ( ( float ) ( Math.random() * 50 % 50 ) - 25f ) * 10f );
                }
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
