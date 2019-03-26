/*
 * This file is part of the Matemaatika Minileksikon.
 * https://github.com/bavuwe/matemaatika
 *
 * Copyright (c) 2019 Bavuwe Software and contributors.
 * See CONTRIBUTORS.txt for details.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bavuwe.matemaatika.listeners;

public interface ContentLoaderListener {

    // Callback to denote that the ContentLoader has finished processing.
    void contentLoaded();
}
