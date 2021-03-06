/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.catalog.domain.product;

import org.seedstack.business.domain.BaseValueObject;

/**
 * A tag value object.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */

public class Tag extends BaseValueObject {

    private String name;

    /**
     * Constructor.
     *
     * @param name the tag value
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * @return the tag name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
