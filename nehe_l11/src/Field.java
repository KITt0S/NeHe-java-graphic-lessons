import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;

public class Field implements GLEventListener {

    private int wiggle_count = 0;
    private int texture;
    private float hold;
    private float xrot, yrot, zrot;
    private float[][][] points = new float[ 45 ][ 45 ][ 3 ];
    private GLU glu = new GLU();

    @Override
    public void init( GLAutoDrawable drawable ) {

        GL2 gl = drawable.getGL().getGL2();
        loadGLTexture( gl );
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0.5f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glPolygonMode( GL2.GL_BACK, GL2.GL_FILL );
        gl.glPolygonMode( GL2.GL_FRONT, GL2.GL_LINE );
        for (int x = 0; x < 45; x++) {

            for (int y = 0; y < 45; y++) {

                points[ x ][ y ][ 0 ] = ( float ) x / 5.0f - 4.5f;
                points[ x ][ y ][ 1 ] = ( float ) y / 5.0f - 4.5f;
                points[ x ][ y ][ 2 ] = ( float ) Math.sin( ( float ) x / 5f  * 40f * Math.PI / 180f );
            }
        }
    }

    private void loadGLTexture( GL2 gl ) {

        try {

            File txtr = new File( "data/flag.jpg" );
            Texture t = TextureIO.newTexture( txtr, true );
            texture = t.getTextureObject();
            TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txtr, true, null  );
            gl.glBindTexture( GL2.GL_TEXTURE_2D,  texture );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, txData.getWidth(), txData.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        } catch ( Exception e ) {

            e.printStackTrace();
        }
    }

    @Override
    public void dispose( GLAutoDrawable drawable ) {

    }

    @Override
    public void display( GLAutoDrawable drawable ) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -17f );
        gl.glRotatef( xrot, 1f, 0f, 0f );
        gl.glRotatef( yrot, 0f, 1f, 0f );
        gl.glRotatef( zrot, 0f, 0f, 1f );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texture );
        float float_x, float_y, float_xb, float_yb;
        gl.glBegin( GL2.GL_QUADS );
        for (int x = 0; x < 44; x++) {

            for (int y = 0; y < 44; y++) {

                float_x = ( float ) x / 44f;
                float_y = ( float ) y / 44f;
                float_xb = ( float ) ( x + 1 ) / 44f;
                float_yb = ( float ) ( y + 1 ) / 44f;

                gl.glTexCoord2f( float_x, float_y );
                gl.glVertex3f( points[ x ][ y ][ 0 ], points[ x ][ y ][ 1 ], points[ x ][ y ][ 2 ] );
                gl.glTexCoord2f( float_x, float_yb );
                gl.glVertex3f( points[ x ][ y + 1 ][ 0 ], points[ x ][ y + 1 ][ 1 ], points[ x ][ y + 1 ][ 2 ] );
                gl.glTexCoord2f( float_xb, float_yb );
                gl.glVertex3f( points[ x + 1 ][ y + 1 ][ 0 ], points[ x + 1 ][ y + 1 ][ 1 ], points[ x + 1 ][ y + 1 ][ 2 ] );
                gl.glTexCoord2f( float_xb, float_y );
                gl.glVertex3f( points[ x + 1 ][ y ][ 0 ], points[ x + 1 ][ y ][ 1 ], points[ x + 1 ][ y ][ 2 ] );
            }
        }
        gl.glEnd();
        gl.glFlush();

        if( wiggle_count == 2 ) {

            for (int y = 0; y < 45; y++) {

                hold = points[ 0 ][ y ][ 2 ];

                for (int x = 0; x < 44; x++) {

                    points[ x ][ y ][ 2 ] = points[ x + 1 ][ y ][ 2 ];
                }
                points[ 44 ][ y ][ 2 ] = hold;
            }
            wiggle_count = 0;
        }
        wiggle_count++;

        xrot += 0.3f;
        yrot += 0.2f;
        zrot += 0.4f;
    }

    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {

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
}
