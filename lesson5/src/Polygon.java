import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Created by KITS on 21.05.2017.
 */
public class Polygon implements GLEventListener {

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glBegin( GL2.GL_POLYGON );
        gl.glVertex3f( -0.5f, -0.5f, 0 );
        gl.glVertex3f( -0.5f, 0.5f, 0 );
        gl.glVertex3f( 0.5f, 0.5f, 0 );
        gl.glVertex3f( 0.5f, -0.5f, 0 );
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
}
