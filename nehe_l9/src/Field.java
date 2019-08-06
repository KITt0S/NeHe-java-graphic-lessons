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
import java.nio.IntBuffer;

public class Field implements GLEventListener, KeyListener {

    private final int NUM = 50; // количество звезд
    private boolean twinkle; // управление мерцанием звезд
    private Star[] stars = new Star[ NUM ];
    private float zoom = -15f; // расстояние до звезд
    private float tilt = 90f; // начальный угол
    private float spin; //вращение звезд
    private int loop;
    private IntBuffer texture = IntBuffer.wrap( new int[ 1 ] );

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        LoadGLTextures( gl );
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f,0.5f );
        gl.glClearDepth( 1f );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE );
        gl.glEnable( GL2.GL_BLEND );
        for ( loop = 0; loop < NUM; loop++ ) {

            Star star = new Star();
            star.angle = 0f;
            star.dist = ( float )loop / ( float )NUM * 5f;
            star.r = ( int ) ( Math.random() * 256 );
            star.g = ( int ) ( Math.random() * 256 );
            star.b = ( int ) ( Math.random() * 256 );
            stars[ loop ] = star;
        }
    }

    private void LoadGLTextures( GL2 gl ) {

        try {

            File srcTxtr = new File( "Star_jpeg.jpg" );
            Texture t = TextureIO.newTexture( srcTxtr, true );
            TextureData data = TextureIO.newTextureData( gl.getGLProfile(), srcTxtr, true, null );
            gl.glGenTextures( 1, texture );
            gl.glBindTexture( GL2.GL_TEXTURE_2D, texture.get( 0 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(), t.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, data.getBuffer() );
        } catch ( IOException e ) {

            e.printStackTrace();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texture.get( 0 ) );
        for ( loop = 0; loop < NUM; loop++ ) {

            gl.glLoadIdentity();
            gl.glTranslatef( 0f, 0f, zoom );
            gl.glRotatef( tilt, 1f, 0f, 0f );
            Star star = stars[ loop ];
            gl.glRotatef( star.angle, 0f, 1f, 0f );
            gl.glTranslatef( star.dist, 0f, 0f );
            gl.glRotatef( -star.angle, 0f, 1f, 0f );
            gl.glRotatef( -tilt, 1f, 0f, 0f );
            star = stars[ NUM - loop - 1 ];
            if( twinkle ) {

                gl.glColor4ub( ( byte )star.r, ( byte )star.g, ( byte )star.b, ( byte )255 );
                gl.glBegin( GL2.GL_QUADS );
                gl.glTexCoord2f( 0f, 0f );
                gl.glVertex3f( -1f, -1f, 0f );
                gl.glTexCoord2f( 1f, 0f );
                gl.glVertex3f( 1f, -1f, 0f );
                gl.glTexCoord2f( 1f, 1f );
                gl.glVertex3f( 1f, 1f, 0f );
                gl.glTexCoord2f( 0f, 1f );
                gl.glVertex3f( -1f, 1f, 0f );
                gl.glEnd();
                gl.glFlush();
            }
            gl.glRotatef( spin, 0f, 0f, 1f );
            star = stars[ loop ];
            gl.glColor4ub( ( byte )star.r, ( byte )star.g, ( byte )star.b, ( byte )255 );
            gl.glBegin( GL2.GL_QUADS );
            gl.glTexCoord2f( 0f, 0f );
            gl.glVertex3f( -1f, -1f, 0f );
            gl.glTexCoord2f( 1f, 0f );
            gl.glVertex3f( 1f, -1f, 0f );
            gl.glTexCoord2f( 1f, 1f );
            gl.glVertex3f( 1f, 1f, 0f );
            gl.glTexCoord2f( 0f, 1f );
            gl.glVertex3f( -1f, 1f, 0f );
            gl.glEnd();
            gl.glFlush();
            spin += 0.01f;
            star.angle += ( float ) loop / ( float )NUM;
            star.dist -= 0.01f;
            if( star.dist < 0f ) {

                star.dist += 5f;
                star.r = ( int ) ( Math.random() * 256 );
                star.g = ( int ) ( Math.random() * 256 );
                star.b = ( int ) ( Math.random() * 256 );
            }
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        if( height == 0 ) {

            height = 1;
        }
        float h = ( float ) width / ( float ) height;
        gl.glViewport( x, y, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluPerspective( 45.0f, h, 0.1f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch( e.getKeyCode() ) {

            case KeyEvent.VK_T: {

                twinkle = !twinkle;
                break;
            }

            case KeyEvent.VK_UP: {

                tilt -= 0.5f;
                break;
            }

            case KeyEvent.VK_DOWN: {

                tilt += 0.5f;
                break;
            }

            case KeyEvent.VK_PAGE_UP: {

                zoom -= 0.2f;
                break;
            }

            case KeyEvent.VK_PAGE_DOWN: {

                zoom += 0.2f;
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

    private class Star {

        private int r, g, b;
        private float dist;
        private float angle;

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public float getDist() {
            return dist;
        }

        public void setDist(float dist) {
            this.dist = dist;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }
}
