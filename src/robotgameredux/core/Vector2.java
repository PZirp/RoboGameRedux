package robotgameredux.core;

/*
 *  Aggiungere metodo equal e clone per l'amor di dio
 */


public class Vector2 implements Cloneable{
	
	/** static temporary vector **/
	private final static Vector2 tmp = new Vector2();
	        
	        /** the x-component of this vector **/
	        private int x;
	        /** the y-component of this vector **/
	        private int y;
	        
	        /**
	         * Constructs a new vector at (0,0)
	         */
	        
	        public int getX() { 
	        	return x;
	        }
	        
	        public int getY() {
	        	return y;
	        }
	        
	        public Vector2( )
	        {
	                
	        }
	        
	        /**
	         * Constructs a vector with the given components
	         * @param x The x-component
	         * @param y The y-component
	         */
	        public Vector2( int x, int y )
	        {
	                this.x = x;
	                this.y = y;
	        }
	        
	        /**
	         * Constructs a vector from the given vector
	         * @param v The vector
	         */
	        public Vector2( Vector2 v )
	        {
	                set( v );
	        }
	        
	        /**
	         * @return a copy of this vector
	         */
	        public Vector2 cpy( )
	        {
	                return new Vector2( this );
	        }
	        
	        /**
	         * @return The euclidian length
	         */
	        public float len( )
	        {
	                return (float)Math.sqrt( x * x + y * y );
	        }
	        
	        /**
	         * @return The squared euclidian length
	         */
	        public float len2( )
	        {
	                return x * x + y * y;
	        }
	        
	        /**
	         * Sets this vector from the given vector
	         * @param v The vector
	         * @return This vector for chaining
	         */
	        public Vector2 set( Vector2 v )
	        {
	                x = v.x;
	                y = v.y;
	                return this;
	        }
	        
	        /**
	         * Sets the components of this vector
	         * @param x The x-component
	         * @param y The y-component
	         * @return This vector for chaining
	         */
	        public Vector2 set( int x, int y )
	        {
	                this.x = x;
	                this.y = y;
	                return this;
	        }
	        
	        /**
	         * Substracts the given vector from this vector.
	         * @param v The vector
	         * @return This vector for chaining
	         */
	        public Vector2 sub( Vector2 v )
	        {
	                x -= v.x;
	                y -= v.y;
	                return this;
	        }
	        
	        /**
	         * Normalizes this vector
	         * @return This vector for chaining
	         */
	        public Vector2 nor( )
	        {
	                float len = len( );
	                if( len != 0 )
	                {
	                        x /= len;
	                        y /= len;
	                }
	                return this;
	        }
	        
	        /**
	         * Adds the given vector to this vector
	         * @param v The vector
	         * @return This vector for chaining
	         */
	        public Vector2 add( Vector2 v )
	        {
	                x += v.x;
	                y += v.y;
	                return this;
	        }
	        
	        /**
	         * Adds the given components to this vector 
	         * @param x The x-component
	         * @param y The y-component
	         * @return This vector for chaining
	         */
	        public Vector2 add( float x, float y )
	        {
	                this.x += x;
	                this.y += y;
	                return this;
	        }
	        
	        /**
	         * @param v The other vector
	         * @return The dot product between this and the other vector
	         */
	        public float dot( Vector2 v )
	        {
	                return x * v.x + y * v.y;
	        }
	        
	        /**
	         * Multiplies this vector by a scalar
	         * @param scalar The scalar
	         * @return This vector for chaining
	         */
	        public Vector2 mul( float scalar )
	        {
	                x *= scalar;
	                y *= scalar;
	                return this;
	        }

	        /**
	         * 
	         * @param v The other vector
	         * @return the distance between this and the other vector
	         */
	        public int dst(Vector2 v) 
	        {       
	                int x_d = v.x - x;
	                int y_d = v.y - y;
	                return (int) Math.sqrt( x_d * x_d + y_d * y_d );
	        }
	        
	        /**
	         * @param x The x-component of the other vector
	         * @param y The y-component of the other vector
	         * @return the distance between this and the other vector
	         */
	        public int dst( int x, int y )
	        {
	                int x_d = x - this.x;
	                int y_d = y - this.y;
	                return (int)Math.sqrt( x_d * x_d + y_d * y_d );
	        }
	        
	        /**
	         * @param v The other vector
	         * @return the squared distance between this and the other vector
	         */
	        public int dst2(Vector2 v)
	        {
	                int x_d = v.x - x;
	                int y_d = v.y - y;
	                return x_d * x_d + y_d * y_d;
	        }       
	        
	        public String toString( )
	        {
	                return  getClass().getName() + "[X = " + x + " Y = " + y + "]";
	        }

	        /**
	         * Substracts the other vector from this vector.
	         * @param x The x-component of the other vector
	         * @param y The y-component of the other vector
	         * @return This vector for chaining
	         */
	        public Vector2 sub(int x, int y) 
	        {
	                this.x -= x;
	                this.y -= y;
	                return this;
	        }
	        
	        

	        /**
	         * Sottrae due vettori senza modificare quello di partenza
	         * @param v1 il primo vettore
	         * @param v2 il secondo vettore
	         * @return un nuovo vettore con il risultato della sottrazione
	         */
	        
	        static public Vector2 sub(Vector2 v1, Vector2 v2) {
	        	return new Vector2((v1.x - v2.x), (v1.y - v2.y));  
	        }
	        
	        /**      
	         * @return a temporary copy of this vector. Use with care as this is backed by a single static Vector2 instance. v1.tmp().add( v2.tmp() ) will not work!
	         */
	        public Vector2 tmp( )
	        {
	                return tmp.set(this);
	        }

	        public Vector2 clone() {
	        	try {
	        		Vector2 clone = (Vector2) super.clone();
	        		return clone;
	        	} catch (CloneNotSupportedException e) {
	        		return null;
	        	}
	        }
	        
	        public boolean equals(Object otherObject) {
	        	if (otherObject == null) return false;
	        	if (getClass() != otherObject.getClass()) return false;
	        	Vector2 other = (Vector2) otherObject;
	        	return x == other.x && y == other.y;
	        }
	        
}
