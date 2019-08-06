import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import jogamp.opengl.glu.GLUquadricImpl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Field implements GLEventListener, KeyListener {

    private int part1;
    private int part2;
    private int p1 = 0;
    private int p2 = 1;
    private int filter;
    private int object = 0;
    private float xRot;
    private float yRot;
    private float xSpeed;
    private float ySpeed;
    private float z = -5f;
    private boolean light;
    private GLUquadric quadratic;
    private FloatBuffer lightAmbient =  FloatBuffer.wrap( new float[]{ 0.5f, 0.5f, 0.5f, 1f } );
    private FloatBuffer lightDiffuse = FloatBuffer.wrap( new float[]{ 1f, 1f, 1f, 1f } );
    private FloatBuffer lightPosition = FloatBuffer.wrap( new float[]{ 0f, 0f, 2f, 1f } );
    private IntBuffer texturesID = IntBuffer.wrap( new int[ 3 ] );
    private GLU glu;

    private void loadGLTexture( GL2 gl ) throws IOException {

        File txFile = new File( "Data/Wall.jpg" );
        Texture texture = TextureIO.newTexture( txFile, true );
        TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txFile, true, null );
        gl.glGenTextures( 3, texturesID );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( 0 ) );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST );
        gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0,
                GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( 1 ) );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
        gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0,
                GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( 2 ) );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_NEAREST );
        glu = GLU.createGLU( gl );
        glu.gluBuild2DMipmaps( GL2.GL_TEXTURE_2D, 3, texture.getWidth(), texture.getHeight(), GL2.GL_RGB,
                GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );

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
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 1f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition );
        gl.glEnable( GL2.GL_LIGHT1 );
        quadratic = glu.gluNewQuadric();
        glu.gluQuadricNormals( quadratic, GL2.GL_SMOOTH );
        glu.gluQuadricTexture( quadratic, true );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    private void drawCube( GL2 gl ) {

        gl.glBegin( GL2.GL_QUADS );

        //передняя грань
        gl.glNormal3f( 0f, 0f, 1f );
        gl.glTexCoord2f( 0f, 0f );
        gl.glVertex3f( -1f, -1f, 1f );
        gl.glTexCoord2f( 1f, 0f );
        gl.glVertex3f( 1f, -1f, 1f );
        gl.glTexCoord2f( 1f, 1f );
        gl.glVertex3f( 1f, 1f, 1f );
        gl.glTexCoord2f( 0f, 1f );
        gl.glVertex3f( -1f, 1f, 1f );

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
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, z );
        gl.glRotatef( xRot, 1f, 0f, 0f );
        gl.glRotatef( yRot, 0f, 1f, 0f );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( filter ) );
        switch( object ) {

            case 0: {

                drawCube( gl );
                break;
            }

            case 1: {

                gl.glTranslatef( 0f, 0f, -1.5f );
                glu.gluCylinder( quadratic, 1f, 1f, 3f, 32, 32 );
                break;
            }

            case 2: {

                glu.gluDisk( quadratic, 0.5f, 1.5f, 32, 32 );
                break;
            }

            case 3: {

                glu.gluSphere( quadratic, 1.3f, 32, 32 );
                break;
            }

            case 4: {

                gl.glTranslatef( 0f, 0f, -1.5f );
                glu.gluCylinder( quadratic, 1f, 0f, 3f, 32, 32 );
                break;
            }

            case 5: {

                part1 += p1;
                part2 += p2;
                if( part1 > 359 ) {

                    p1 = 0;
                    part1 = 0;
                    p2 = 1;
                    part2 = 0;
                }
                if( part2 > 359 ) {

                    p1 = 1;
                    p2 = 0;
                }
                glu.gluPartialDisk( quadratic, 0.5f, 1.5f, 32, 32, part1, part2 - part1 );
                break;
            }
        }
        xRot += xSpeed;
        yRot += ySpeed;

        if( light ) {

            gl.glEnable( GL2.GL_LIGHTING );
        } else {

            gl.glDisable( GL2.GL_LIGHTING );
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport( 0, 0 , width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        if( height == 0 ) {

            height = 1;
        }
        float h = ( float ) width / ( float ) height;
        glu.gluPerspective( 45, h, 01f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch ( e.getKeyCode() ) {

            case KeyEvent.VK_L: {

                light = !light;
                break;
            }

            case KeyEvent.VK_F: {

                filter++;
                if( filter > 2 ) {

                    filter = 0;
                }
                System.out.println( filter );
                break;
            }

            case KeyEvent.VK_SPACE: {

                object++;
                if( object > 5 ) {

                    object = 0;
                }
                System.out.println( object );
                break;
            }

            case KeyEvent.VK_LEFT: {

                ySpeed -= 0.1f;
                break;
            }

            case KeyEvent.VK_RIGHT: {

                ySpeed += 0.1f;
                break;
            }

            case KeyEvent.VK_UP: {

                xSpeed -= 0.1f;
                break;
            }

            case KeyEvent.VK_DOWN: {

                xSpeed += 0.1f;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
