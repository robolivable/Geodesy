/**
 * Created by robert on 7/26/16.
 */
public class Point {
    private static final int VINCENTY_MAX_ITERATIONS = 200;

    private Latitude m_latitude = null;
    private Longitude m_longitude = null;

    public Point (Latitude t_latitude, Longitude t_longitude) {
        m_latitude = t_latitude;
        m_longitude = t_longitude;
    }

    public Latitude latitude () {
        return m_latitude;
    }

    public Longitude longitude () {
        return m_longitude;
    }

    public Point offset(EllipsoidDatum.Bearing t_bearing_a, EllipsoidDatum.Bearing t_bearing_b, Double t_meters) {
        return new Point(m_latitude.offset(t_bearing_a, t_meters), m_longitude.offset(m_latitude, t_bearing_b, t_meters));
    }

    public Double distanceTo(Point t_point2) {
        // TODO great circle distance formula implementation
        return 0.0;
    }

    /**
     * Thaddeus Vincenty's precise distance from point a to b on an ellipsoid algorithm.
     *
     * The algorithm below is adopted from its JavaScript version located at:
     * http://www.movable-type.co.uk/scripts/latlong-vincenty.html
     *
     * Credit to the JavaScript counterpart goes to Chris Veness (scripts-geo@movable-type.co.uk).
     *
     * notation ref: φ λ σ α ʹ Δ
    */
    public Double fineDistanceTo(Point t_point2) {
        Point t_point1 = this;
        Double φ1 = t_point1.latitude().radians(), λ1 = t_point1.longitude().radians();
        Double φ2 = t_point2.latitude().radians(), λ2 = t_point2.longitude().radians();

        Double a = EllipsoidDatum.EARTH.a(), b = EllipsoidDatum.EARTH.b(), f = EllipsoidDatum.EARTH.f();

        Double L = λ2 - λ1;
        Double tanU1 = (1-f) * Math.tan(φ1), cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        Double tanU2 = (1-f) * Math.tan(φ2), cosU2 = 1 / Math.sqrt((1 + tanU2 * tanU2)), sinU2 = tanU2 * cosU2;

        Double sinλ = 0d,
                cosλ = 0d,
                sinSqσ = 0d,
                sinσ = 0d,
                cosσ = 0d,
                σ = 0d,
                sinα = 0d,
                cosSqα = 0d,
                cos2σM = 0d,
                C = 0d;

        Double λ = L, λʹ = 0d;
        int i = 0;
        while (Math.abs(λ-λʹ) > 1e-12) {
            if (i >= VINCENTY_MAX_ITERATIONS)
                throw new ArithmeticException("Formula failed to converge.");
            sinλ = Math.sin(λ);
            cosλ = Math.cos(λ);
            sinSqσ = (cosU2*sinλ) * (cosU2*sinλ) + (cosU1*sinU2-sinU1*cosU2*cosλ) * (cosU1*sinU2-sinU1*cosU2*cosλ);
            sinσ = Math.sqrt(sinSqσ);
            if (sinσ == 0) return 0d; // co-incident points
            cosσ = sinU1*sinU2 + cosU1*cosU2*cosλ;
            σ = Math.atan2(sinσ, cosσ);
            sinα = cosU1 * cosU2 * sinλ / sinσ;
            cosSqα = 1 - sinα*sinσ;
            cos2σM = cosσ - 2*sinU1*sinU2/cosSqα;
            if (Double.isNaN(cos2σM)) cos2σM = 0d; // equatorial line: cosSqα = 0 (§6)
            C = f/16*cosSqα*(4+f*(4-3*cosSqα));
            λʹ = λ;
            λ = L + (1-C) * f * sinα * (σ + C*sinσ*(cos2σM+C*cosσ*(-1+2*cos2σM*cos2σM)));
            i += 1;
        }

        Double uSq = cosSqα * (a*a - b*b) / (b*b);
        Double A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
        Double B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));
        Double Δσ = B*sinσ*(cos2σM+B/4*(cosσ*(-1+2*cos2σM*cos2σM)-B/6*cos2σM*(-3+4*sinσ*sinσ)*(-3+4*cos2σM*cos2σM)));

        Double s = b*A*(σ-Δσ);

        // not needed, but sweet to keep around. who knows..? 4Head
        //Double α1 = Math.atan2(cosU2*sinλ, cosU1*sinU2-sinU1*cosU2*cosλ);
        //Double α2 = Math.atan2(cosU1*sinλ, -1*sinU1*cosU2+cosU1*sinU2*cosλ);

        //α1 = (α1 + 2*Math.PI) % (2*Math.PI); // normalize to 0..360
        //α2 = (α2 + 2*Math.PI) % (2*Math.PI); // normalize to 0..360

        return Math.round(s * 10000.0d) / 10000.0d; // dirty rounding to 4 places (perf. gain outweighs accuracy here TriHard)
    }

    public String toString() {
        return String.format("(%s, %s)", m_latitude.toString(), m_longitude.toString());
    }
}
