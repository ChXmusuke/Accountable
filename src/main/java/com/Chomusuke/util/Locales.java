/*  Accountable: a personal spending monitoring program
    Copyright (C) 2022  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.Chomusuke.util;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Provides a Resource Bundle for managing locales.
 */
public final class Locales {

    public static ResourceBundle messages = ResourceBundle.getBundle("resources.messages.messages", Locale.ENGLISH);

    /**
     * Don't let anyone instantiate this class.
     */
    private Locales() {
    }
}
