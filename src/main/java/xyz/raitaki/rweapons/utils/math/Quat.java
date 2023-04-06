package xyz.raitaki.rweapons.utils.math;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.bukkit.util.Vector;

/*
 * Copyright (c) 2014 Kamil Kolaczynski
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kamil Kolaczynski (revers) - initial implementation
 */

/*
 * revers: My own implementation of quaternion.
 */
public class Quat {
    public float x, y, z, w;

    public Quat() {
        // x = y = z = 0.0f;
        w = 1.0f;
    }

    public Quat(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void identity() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        w = 1.0f;
    }

    public float pitch() {
        return (float) Math.atan2(2.0 * (y * z + w * x), w * w - x * x - y * y + z * z);
    }

    public float yaw() {
        return (float) Math.asin(-2.0 * (x * z - w * y));
    }

    public float roll() {
        return (float) Math.atan2(2.0 * (x * y + w * z), w * w + x * x - y * y - z * z);
    }

    public int getGimbalPole() {
        final float t = y * x + z * w;
        return t > 0.499f ? 1 : (t < -0.499f ? -1 : 0);
    }

    /**
     * Get the roll euler angle in radians, which is the rotation around the z
     * axis. Requires that this quaternion is normalized.
     *
     * @return the rotation around the z axis in radians (between -PI and +PI)
     */
    public float getRollRad() {
        final int pole = getGimbalPole();
        return (float) (pole == 0 ? Math.atan2(2f * (w * z + y * x), 1f - 2f * (x * x + z * z)) : (float) pole * 2f * Math.atan2(y, w));
    }

    /**
     * Get the roll euler angle in degrees, which is the rotation around the z
     * axis. Requires that this quaternion is normalized.
     *
     * @return the rotation around the z axis in degrees (between -180 and +180)
     */

    /**
     * Get the pitch euler angle in radians, which is the rotation around the x
     * axis. Requires that this quaternion is normalized.
     *
     * @return the rotation around the x axis in radians (between -(PI/2) and
     * +(PI/2))
     */
    public float getPitchRad() {
        final int pole = getGimbalPole();
        return (float) (pole == 0 ? (float) Math.asin(clamp(2f * (w * x - z * y), -1f, 1f)) : (float) pole * Math.PI * 0.5f);
    }

    /**
     * Get the pitch euler angle in degrees, which is the rotation around the x
     * axis. Requires that this quaternion is normalized.
     *
     * @return the rotation around the x axis in degrees (between -90 and +90)
     */

    static public short clamp(short value, short min, short max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    static public int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    static public long clamp(long value, long min, long max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    static public float clamp(float value, float min, float max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    static public double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Get the yaw euler angle in radians, which is the rotation around the y
     * axis. Requires that this quaternion is normalized.
     *
     * @return the rotation around the y axis in radians (between -PI and +PI)
     */
    public float getYawRad() {
        return (float) (getGimbalPole() == 0 ? Math.atan2(2f * (y * w + x * z), 1f - 2f * (y * y + x * x)) : 0f);
    }

    /**
     * Get the yaw euler angle in degrees, which is the rotation around the y
     * axis. Requires that this quaternion is normalized.
     *
     * @return the rotation around the y axis in degrees (between -180 and +180)
     */

    /**
     * Saves Euler Angles (pitch, yaw, roll) in the <code>result</code> Vector
     * (x, y, z).
     */
    public Vector3f getEulerAngles(Vector3f result) {
        result.x = (float) Math.atan2(2.0 * (y * z + w * x), w * w - x * x - y * y + z * z);
        result.y = (float) Math.asin(-2.0 * (x * z - w * y));
        result.z = (float) Math.atan2(2.0 * (x * y + w * z), w * w + x * x - y * y - z * z);
        return result;
    }

    public void getRotationQuat(Vector from, Vector to) {

        Vector H = from.clone().add(to);
        H.normalize();

        this.w = (float) from.clone().dot(H);
        this.x = (float) (from.getY() * H.getZ() - from.getZ() * H.getY());
        this.y = (float) (from.getZ() * H.getX() - from.getX() * H.getZ());
        this.z = (float) (from.getX() * H.getY() - from.getY() * H.getX());

    }

    /**
     * Build a quaternion from euler angles (pitch, yaw, roll), in radians.
     */
    public Quat fromEulerAngles(float pitch, float yaw, float roll) {
        float cx = (float) Math.cos(pitch * 0.5);
        float cy = (float) Math.cos(yaw * 0.5);
        float cz = (float) Math.cos(roll * 0.5);

        float sx = (float) Math.sin(pitch * 0.5);
        float sy = (float) Math.sin(yaw * 0.5);
        float sz = (float) Math.sin(roll * 0.5);

        this.w = cx * cy * cz + sx * sy * sz;
        this.x = sx * cy * cz - cx * sy * sz;
        this.y = cx * sy * cz + sx * cy * sz;
        this.z = cx * cy * sz - sx * sy * cz;

        return this;
    }

    public Quat fromAngleNormalAxis(float angle, float axis_x, float axis_y, float axis_z) {
        float s = (float) Math.sin(angle * 0.5);

        w = (float) Math.cos(angle * 0.5);
        x = axis_x * s;
        y = axis_y * s;
        z = axis_z * s;

        return this;
    }

    public Quat lookAt(Vector direction, Vector up) {

        Vector vect3 = direction.clone().normalize();
        Vector vect1 = up.clone().crossProduct(direction.clone()).normalize();
        Vector vect2 = direction.clone().crossProduct(vect1).normalize();

        fromAxes(vect1, vect2, vect3);
        this.normalizeLocal();

        return this;
    }

    public Quat normalizeLocal() {
        float n = 1.0f / (float) Math.sqrt(w * w + x * x + y * y + z * z);
        x *= n;
        y *= n;
        z *= n;
        w *= n;
        return this;
    }

    public Quat fromAxes(final Vector xAxis, final Vector yAxis, final Vector zAxis) {
        return fromRotationMatrix((float) xAxis.getX(), (float) yAxis.getX(), (float) zAxis.getX(), (float) xAxis.getY(), (float) yAxis.getY(), (float) zAxis.getY(), (float) xAxis.getZ(), (float) yAxis.getZ(), (float) zAxis.getZ());
    }

    /**
     * @param q      the quaternion to multiply this quaternion by.
     * @param result the quaternion to store the result in.
     * @return the new quaternion.
     */
    public Quat mult(Quat q, Quat result) {
        float qw = q.w;
        float qx = q.x;
        float qy = q.y;
        float qz = q.z;
        result.x = x * qw + y * qz - z * qy + w * qx;
        result.y = -x * qz + y * qw + z * qx + w * qy;
        result.z = x * qy - y * qx + z * qw + w * qz;
        result.w = -x * qx - y * qy - z * qz + w * qw;
        return result;
    }

    public Quat set(Quat q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
        return this;
    }

    public Quat set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quat conjugate(Quat result) {
        result.w = w;
        result.x = -x;
        result.y = -y;
        result.z = -z;

        return result;
    }

    /**
     * http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
     * TODO: Fix the function.
     *
     * @deprecated This function probably has some computational mistake. I'll
     * try to fix it some day.
     */
    public Vector3f rotate(final Vector3f v, Vector3f result) {
        // p = (v.x, v.y, v.z, 0.0f)
        // Result equation:
        // result = q * p * conjugate(q);

        // t = p * conjugate(q)
        float t_x = v.x * w + v.y * (-z) - v.z * (-y); /* + 0.0f * (-x); */
        float t_y = -v.x * (-z) + v.y * w + v.z * (-x); /* + 0.0f * (-y); */
        float t_z = v.x * (-y) - v.y * (-x) + v.z * w; /* + 0.0f * (-z); */
        float t_w = -v.x * (-x) - v.y * (-y) - v.z * (-z); /* + 0.0f * w; */

        // result = q * t
        result.x = x * t_w + y * t_z - z * t_y + w * t_x;
        result.y = -x * t_z + y * t_w + z * t_x + w * t_y;
        result.z = x * t_y - y * t_x + z * t_w + w * t_z;
        // w = -x * t_x - y * t_y - z * t_z + w * t_w;

        return result;
    }

    /**
     * Converts Euler angles to a matrix, using intermediate quaternion. The
     * result matrix is in column-major order.
     *
     * @param rotMatrix should be 9 element array
     */
    public static float[] toMatrix3(float[] rotMatrix, float pitch, float yaw, float roll) {
        float cx = (float) Math.cos(pitch * 0.5);
        float cy = (float) Math.cos(yaw * 0.5);
        float cz = (float) Math.cos(roll * 0.5);

        float sx = (float) Math.sin(pitch * 0.5);
        float sy = (float) Math.sin(yaw * 0.5);
        float sz = (float) Math.sin(roll * 0.5);

        float q_w = cx * cy * cz + sx * sy * sz;
        float q_x = sx * cy * cz - cx * sy * sz;
        float q_y = cx * sy * cz + sx * cy * sz;
        float q_z = cx * cy * sz - sx * sy * cz;

        // m[0][0]
        rotMatrix[0] = 1 - 2 * q_y * q_y - 2 * q_z * q_z;
        // m[0][1]
        rotMatrix[1] = 2 * q_x * q_y + 2 * q_w * q_z;
        // m[0][2]
        rotMatrix[2] = 2 * q_x * q_z - 2 * q_w * q_y;

        // m[1][0]
        rotMatrix[3] = 2 * q_x * q_y - 2 * q_w * q_z;
        // m[1][1]
        rotMatrix[4] = 1 - 2 * q_x * q_x - 2 * q_z * q_z;
        // m[1][2]
        rotMatrix[5] = 2 * q_y * q_z + 2 * q_w * q_x;

        // m[2][0]
        rotMatrix[6] = 2 * q_x * q_z + 2 * q_w * q_y;
        // m[2][1]
        rotMatrix[7] = 2 * q_y * q_z - 2 * q_w * q_x;
        // m[2][2]
        rotMatrix[8] = 1 - 2 * q_x * q_x - 2 * q_y * q_y;

        return rotMatrix;
    }

    /**
     * Converts Euler angles to a matrix, using intermediate quaternion. The
     * result matrix is in column-major order.
     *
     * @param rotMatrix should be 16 element array
     */
    public static float[] toMatrix4(float[] rotMatrix, float pitch, float yaw, float roll) {
        float cx = (float) Math.cos(pitch * 0.5);
        float cy = (float) Math.cos(yaw * 0.5);
        float cz = (float) Math.cos(roll * 0.5);

        float sx = (float) Math.sin(pitch * 0.5);
        float sy = (float) Math.sin(yaw * 0.5);
        float sz = (float) Math.sin(roll * 0.5);

        float q_w = cx * cy * cz + sx * sy * sz;
        float q_x = sx * cy * cz - cx * sy * sz;
        float q_y = cx * sy * cz + sx * cy * sz;
        float q_z = cx * cy * sz - sx * sy * cz;

        // m[0][0]
        rotMatrix[0] = 1 - 2 * q_y * q_y - 2 * q_z * q_z;
        // m[0][1]
        rotMatrix[1] = 2 * q_x * q_y + 2 * q_w * q_z;
        // m[0][2]
        rotMatrix[2] = 2 * q_x * q_z - 2 * q_w * q_y;
        // m[0][3]
        rotMatrix[3] = 0;

        // m[1][0]
        rotMatrix[4] = 2 * q_x * q_y - 2 * q_w * q_z;
        // m[1][1]
        rotMatrix[5] = 1 - 2 * q_x * q_x - 2 * q_z * q_z;
        // m[1][2]
        rotMatrix[6] = 2 * q_y * q_z + 2 * q_w * q_x;
        // m[1][3]
        rotMatrix[7] = 0;

        // m[2][0]
        rotMatrix[8] = 2 * q_x * q_z + 2 * q_w * q_y;
        // m[2][1]
        rotMatrix[9] = 2 * q_y * q_z - 2 * q_w * q_x;
        // m[2][2]
        rotMatrix[10] = 1 - 2 * q_x * q_x - 2 * q_y * q_y;
        // m[2][3]
        rotMatrix[11] = 0;

        // m[3][0]
        rotMatrix[12] = 0;
        // m[3][1]
        rotMatrix[13] = 0;
        // m[3][2]
        rotMatrix[14] = 0;
        // m[3][3]
        rotMatrix[15] = 1;

        return rotMatrix;
    }

    /**
     * Converts quaternion to matrix. The result matrix is in column-major
     * order.
     *
     * @param rotMatrix should be 9 element array
     */
    public float[] toMatrix3(float[] rotMatrix) {
        // m[0][0]
        rotMatrix[0] = 1 - 2 * y * y - 2 * z * z;
        // m[0][1]
        rotMatrix[1] = 2 * x * y + 2 * w * z;
        // m[0][2]
        rotMatrix[2] = 2 * x * z - 2 * w * y;

        // m[1][0]
        rotMatrix[3] = 2 * x * y - 2 * w * z;
        // m[1][1]
        rotMatrix[4] = 1 - 2 * x * x - 2 * z * z;
        // m[1][2]
        rotMatrix[5] = 2 * y * z + 2 * w * x;

        // m[2][0]
        rotMatrix[6] = 2 * x * z + 2 * w * y;
        // m[2][1]
        rotMatrix[7] = 2 * y * z - 2 * w * x;
        // m[2][2]
        rotMatrix[8] = 1 - 2 * x * x - 2 * y * y;

        return rotMatrix;
    }

    /**
     * Converts quaternion to matrix. The result matrix is in column-major
     * order.
     *
     * @param rotMatrix should be 16 element array
     */
    public float[] toMatrix4(float[] rotMatrix) {
        // m[0][0]
        rotMatrix[0] = 1 - 2 * y * y - 2 * z * z;
        // m[0][1]
        rotMatrix[1] = 2 * x * y + 2 * w * z;
        // m[0][2]
        rotMatrix[2] = 2 * x * z - 2 * w * y;
        // m[0][3]
        rotMatrix[3] = 0;

        // m[1][0]
        rotMatrix[4] = 2 * x * y - 2 * w * z;
        // m[1][1]
        rotMatrix[5] = 1 - 2 * x * x - 2 * z * z;
        // m[1][2]
        rotMatrix[6] = 2 * y * z + 2 * w * x;
        // m[1][3]
        rotMatrix[7] = 0;

        // m[2][0]
        rotMatrix[8] = 2 * x * z + 2 * w * y;
        // m[2][1]
        rotMatrix[9] = 2 * y * z - 2 * w * x;
        // m[2][2]
        rotMatrix[10] = 1 - 2 * x * x - 2 * y * y;
        // m[2][3]
        rotMatrix[11] = 0;

        // m[3][0]
        rotMatrix[12] = 0;
        // m[3][1]
        rotMatrix[13] = 0;
        // m[3][2]
        rotMatrix[14] = 0;
        // m[3][3]
        rotMatrix[15] = 1;

        return rotMatrix;
    }

    public Matrix3f toMatrix3(Matrix3f result) {
        result.m00 = 1 - 2 * y * y - 2 * z * z;
        result.m10 = 2 * x * y + 2 * w * z;
        result.m20 = 2 * x * z - 2 * w * y;

        result.m01 = 2 * x * y - 2 * w * z;
        result.m11 = 1 - 2 * x * x - 2 * z * z;
        result.m21 = 2 * y * z + 2 * w * x;

        result.m02 = 2 * x * z + 2 * w * y;
        result.m12 = 2 * y * z - 2 * w * x;
        result.m22 = 1 - 2 * x * x - 2 * y * y;

        return result;
    }

    public Matrix4f toMatrix4(Matrix4f result) {
        result.m00 = 1 - 2 * y * y - 2 * z * z;
        result.m10 = 2 * x * y + 2 * w * z;
        result.m20 = 2 * x * z - 2 * w * y;
        result.m30 = 0;

        result.m01 = 2 * x * y - 2 * w * z;
        result.m11 = 1 - 2 * x * x - 2 * z * z;
        result.m21 = 2 * y * z + 2 * w * x;
        result.m31 = 0;

        result.m02 = 2 * x * z + 2 * w * y;
        result.m12 = 2 * y * z - 2 * w * x;
        result.m22 = 1 - 2 * x * x - 2 * y * y;
        result.m32 = 0;

        result.m03 = 0;
        result.m13 = 0;
        result.m23 = 0;
        result.m33 = 1;

        return result;
    }

    public Quat fromRotationMatrix(Matrix3f matrix) {
        return fromRotationMatrix(matrix.m00, matrix.m01, matrix.m02, matrix.m10, matrix.m11, matrix.m12, matrix.m20, matrix.m21, matrix.m22);
    }

    public Quat fromRotationMatrix(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        // Use the Graphics Gems code, from
        // ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z
        // *NOT* the "Matrix and Quaternions FAQ", which has errors!

        // the trace is the sum of the diagonal elements; see
        // http://mathworld.wolfram.com/MatrixTrace.html
        float t = m00 + m11 + m22;

        // we protect the division by s by ensuring that s>=1
        if (t >= 0) { // |w| >= .5
            float s = (float) Math.sqrt(t + 1); // |s|>=1 ...
            w = 0.5f * s;
            s = 0.5f / s; // so this division isn't bad
            x = (m21 - m12) * s;
            y = (m02 - m20) * s;
            z = (m10 - m01) * s;
        } else if ((m00 > m11) && (m00 > m22)) {
            float s = (float) Math.sqrt(1.0f + m00 - m11 - m22); // |s|>=1
            x = s * 0.5f; // |x| >= .5
            s = 0.5f / s;
            y = (m10 + m01) * s;
            z = (m02 + m20) * s;
            w = (m21 - m12) * s;
        } else if (m11 > m22) {
            float s = (float) Math.sqrt(1.0f + m11 - m00 - m22); // |s|>=1
            y = s * 0.5f; // |y| >= .5
            s = 0.5f / s;
            x = (m10 + m01) * s;
            z = (m21 + m12) * s;
            w = (m02 - m20) * s;
        } else {
            float s = (float) Math.sqrt(1.0f + m22 - m00 - m11); // |s|>=1
            z = s * 0.5f; // |z| >= .5
            s = 0.5f / s;
            x = (m02 + m20) * s;
            y = (m21 + m12) * s;
            w = (m10 - m01) * s;
        }

        return this;
    }

    public void slerp(Quat a, Quat b, float t) {
        // detail::tquat<T> c = b;
        float c_x = b.x;
        float c_y = b.y;
        float c_z = b.z;
        float c_w = b.w;

        // cosTheta = dot(a, b);
        float cosTheta = a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;

        // If cosTheta < 0, the interpolation will take the long way around the
        // sphere.
        // To fix this, one quat must be negated.
        if (cosTheta < 0.0f) {
            // c = -b;
            c_x = -b.x;
            c_y = -b.y;
            c_z = -b.z;
            c_w = -b.w;

            cosTheta = -cosTheta;
        }
        float t0 = 1.0f - t;
        float t1 = t;

        // Perform a linear interpolation when cosTheta is close to 1 to avoid
        // side effect of sin(angle) becoming a zero denominator
        if (cosTheta <= 1.0f - 0.05f) {
            // Essential Mathematics, page 467
            float angle = (float) Math.acos(cosTheta);
            float p = (float) Math.sin(t0 * angle);
            float q = (float) Math.sin(t1 * angle);
            float d = (float) Math.sin(angle);
            float dInv = 1.0f / d;

            t0 = p * dInv;
            t1 = q * dInv;
        }
        this.x = t0 * a.x + t1 * c_x;
        this.y = t0 * a.y + t1 * c_y;
        this.z = t0 * a.z + t1 * c_z;
        this.w = t0 * a.w + t1 * c_w;
    }

    @Override
    public String toString() {
        return "Quat [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }
}
