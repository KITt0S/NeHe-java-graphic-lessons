import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;


import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Field implements GLEventListener, KeyListener {

    private float rCubeX, rCubeY;
    private float rCubeSpeedX, rCubeSpeedY;
    private float zTransl;
    private FloatBuffer lightAmbient = FloatBuffer.wrap( new float[] { 0.5f, 0.5f, 0.5f, 1.0f } );
    private FloatBuffer lightDiffuse = FloatBuffer.wrap( new float[] { 1.0f, 1.0f, 1.0f, 1.0f } );
    private FloatBuffer lightPosition = FloatBuffer.wrap( new float[] { 0.0f, 0.0f, 2.0f, 1.0f } );
    private IntBuffer textures = IntBuffer.wrap( new int[ 3 ] );
    private int texture;
    private int filter = 0;
    private boolean light;
    private boolean blend;
    private GLU glu = new GLU();

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        LoadGLTextures( gl );
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0.5f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition );
        gl.glEnable( GL2.GL_LIGHT1 );
        gl.glColor4f( 1f, 1f, 1f, 0.5f );
        gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    private void LoadGLTextures( GL2 gl ) {

        try {

            File txtr = new File( "Glass_jpeg.jpg" );
            Texture t = TextureIO.newTexture( txtr, true );
            texture = t.getTextureObject();
            //TextureData txData = AWTTextureIO.newTextureData( gl.getGLProfile(), ImageIO.read( txtr ), true );
            TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txtr, true, null );

            gl.glGenTextures( 3, textures );

            gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( 0 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(),  t.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );

            gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( 1 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(), t.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );

            gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( 2 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_NEAREST );
            GLU glu = GLU.createGLU( gl );
            glu.gluBuild2DMipmaps( GL2.GL_TEXTURE_2D, 3, t.getWidth(), t.getHeight(), GL2.GL_RGB,
                    GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        } catch ( Exception e ) {

            e.printStackTrace();
        }
    }

    private Buffer getTextureData( File fImage ) {

        try {

            BufferedImage image = ImageIO.read( fImage );
            int[] pixels = new int[ image.getWidth() * image.getHeight() ];
            ByteBuffer data = ByteBuffer.allocate( pixels.length * 4 );
            for (int y = 0; y < image.getHeight(); y++) {

                for (int x = 0; x < image.getWidth(); x++) {

                    int pixel = image.getRGB( x, y );
                    data.put((byte) ((pixel >> 16) & 0xFF));    // Red component
                    data.put((byte) ((pixel >> 8) & 0xFF));     // Green component
                    data.put((byte) (pixel & 0xFF));            // Blue component
                    data.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }
            data.flip();
            return data;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -6f + zTransl );
        gl.glRotatef( rCubeX, 1f, 0f, 0f );
        gl.glRotatef( rCubeY, 0f, 1f, 0f );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( filter ) );

        gl.glBegin( GL2.GL_QUADS );

        //передняя грань
        gl.glNormal3f( 0.0f, 0.0f, 1.0f );
        gl.glTexCoord2f( 0.0f, 0.0f );
        gl.glVertex3f( -1.0f, -1.0f, 1.0f );
        gl.glTexCoord2f( 1.0f, 0.0f );
        gl.glVertex3f( 1.0f, -1.0f, 1.0f );
        gl.glTexCoord2f( 1.0f, 1.0f );
        gl.glVertex3f( 1.0f, 1.0f, 1.0f );
        gl.glTexCoord2f( 0.0f, 1.0f );
        gl.glVertex3f( -1.0f, 1.0f, 1.0f );

        //задняя грань
        gl.glNormal3f( 0.0f, 0.0f, -1.0f );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, -1 );

        //левая грань
        gl.glNormal3f( -1.0f,0.0f, 0.0f);
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, 1, 1 );

        //правая грань
        gl.glNormal3f( 1.0f,0.0f, 0.0f);
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, 1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, -1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, 1, 1 );

        //верхняя грань
        gl.glNormal3f( 0.0f,1.0f, 0.0f);
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, 1, 1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, 1, 1 );

        //нижняя грань
        gl.glNormal3f( 0.0f,-1.0f, 0.0f);
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, -1, -1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, 1 );

        gl.glEnd();
        gl.glFlush();

        rCubeX += rCubeSpeedX;
        rCubeY += rCubeSpeedY;

        if( light ) {

            gl.glEnable( GL2.GL_LIGHTING );
        } else {

            gl.glDisable( GL2.GL_LIGHTING );
        }

        if( blend ) {

            gl.glEnable( GL2.GL_BLEND );
            gl.glDisable( GL2.GL_DEPTH_TEST );
        } else {

            gl.glDisable( GL2.GL_BLEND );
            gl.glEnable( GL2.GL_DEPTH_TEST );
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        if( height == 0 ) {

            height = 1;
        }
        float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        glu.gluPerspective( 45f, h, 0.1f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch ( e.getKeyCode() ) {

            case KeyEvent.VK_LEFT: {

                rCubeSpeedY -= 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_RIGHT: {

                rCubeSpeedY += 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_DOWN: {

                rCubeSpeedX -= 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_UP: {

                rCubeSpeedX += 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_H: {

                zTransl += 0.1f;
                System.out.println( "Координата Z: " + String.format( "%.1f", zTransl ) );
                break;
            }

            case KeyEvent.VK_G: {

                zTransl -= 0.1f ;
                System.out.println( "Координата Z: " + String.format( "%.1f", zTransl ) );
                break;
            }

            case KeyEvent.VK_F: {

                if( filter != 2 ) {

                    filter ++;
                } else {

                    filter = 0;
                }
                System.out.println( "фильтр: " + "\"" + filter + "\"" );
                break;
            }

            case KeyEvent.VK_L: {

                light = !light;

                if( light ) {

                    System.out.println( "Освещение включено" );
                } else  {

                    System.out.println("Освещение выключено" );
                }
                break;
            }

            case KeyEvent.VK_B: {

                blend = !blend;
                if( blend ) {

                    System.out.println( "Смешение включено" );
                } else {

                    System.out.println( "Смешение выключено" );
                }
                break;
            }

            default: {

                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
