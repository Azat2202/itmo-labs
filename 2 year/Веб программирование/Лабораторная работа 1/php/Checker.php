<?php

namespace php;
class Checker
{
    const X_MIN = -3;
    const X_MAX = 5;
    const Y_MIN = -5;
    const Y_MAX = 5;
    const R_MIN = 1;
    const R_MAX = 5;

    public function validate($x, $y, $r)
    {
        if (!is_numeric($x) || !is_numeric($y) || !is_numeric($r)) {
            return false;
        } else {
            $y_num = floatval($y);
            $x_num = intval($x);
            $r_num = intval($r);


            if (!($x_num >= self::X_MIN && $x_num <= self::X_MAX)) {
                return false;
            }

            if (!($y_num >= self::Y_MIN && $y_num <= self::Y_MAX)) {
                return false;
            }

            if (!($r_num >= self::R_MIN && $r_num <= self::R_MAX)) {
                return false;
            }

            return true;
        }
    }

    public function check($x, $y, $r)
    {
        if ($x >= 0 && $y >= 0 && ($x > $r / 2 || $y > $r)) return false;
        elseif ($x < 0 && $y > 0 && ($y > $x / 2 + $r / 2)) return false;
        elseif ($x < 0 && $y < 0 && ($x * $x + $y * $y > $r * $r)) return false;
        elseif ($x > 0 && $y < 0) return false;
        return true;
    }
}