import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;

public class Field implements GLEventListener {

    private float cnt1, cnt2;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 1f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -2f );
//        gl.glColor3f( ( float ) Math.cos( cnt1 ), ( float ) Math.sin( cnt2 ), 1f - 0.5f *
//                ( float ) Math.cos( cnt1 + cnt2 ) );
//        gl.glRasterPos2f( -0.45f + 0.05f * ( float ) Math.cos( cnt1 ), 0.35f * ( float ) Math.sin( cnt2 ) );
//        glut.glutBitmapString( GLUT.BITMAP_TIMES_ROMAN_24, "Active OpenGL Text With NeHe - " + cnt1 );
        TextRenderer renderer = new TextRenderer( new Font( "Times New Roman", Font.BOLD, 72 ) );
        renderer.begin3DRendering();
        renderer.setColor( ( float ) Math.cos( cnt1 ), ( float ) Math.sin( cnt2 ), 1f - 0.5f *
                        ( float ) Math.cos( cnt1 + cnt2 ), 1f );
        renderer.draw3D( String.format( "Active OpenGL Text With NeHe - %.2f", cnt1 ), -0.45f + 0.05f * ( float ) Math.cos( cnt1 ),
                0.35f * ( float ) Math.sin( cnt2 ), 0f, 0.0008f  );
        renderer.end3DRendering();
        cnt1 += 0.051f;
        cnt2 += 0.005f;
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
        glu.gluPerspective( 45, h, 0.1f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }
}
