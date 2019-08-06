import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Created by KITS on 21.05.2017.
 */
public class Line implements GLEventListener {

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl2 = drawable.getGL().getGL2();
        gl2.glBegin( GL2.GL_LINES );
        gl2.glVertex3f( 0.5f, -0.5f, 0);
        gl2.glVertex3f( -0.5f, 0.5f, 0);
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
}
