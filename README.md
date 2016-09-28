# Geodesy
Straight forward distance calculation.

### Classes
- `Latitude`: Initialize with `Double`. Value must be within valid range.
    - `degrees()` - Get value in degrees.
    - `radians()` - Get value in radians.
- `Longitude`: Initialize with `Double`. Value must be within valid range.
    - `degrees()` - Get value in degrees.
    - `radians()` - Get value in radians.
- `Point`: Initialize with `Latitude` and `Longitude`.
    - `distanceTo(Point)` - Get a distance calculation from this point to a given point.
    - `fineDistanceTo(Point)` - Get an accurate distance calculation from this point to a given point. This method uses Thaddeus Vincenty's precise distance from point a to b on an ellipsoid algorithm. The code was adopted from its JavaScript version located at: http://www.movable-type.co.uk/scripts/latlong-vincenty.html. Credit to the JavaScript counterpart goes to Chris Veness (scripts-geo@movable-type.co.uk).
