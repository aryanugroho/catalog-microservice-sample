/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.catalog;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.samples.catalog.domain.product.Product;
import org.seedstack.samples.catalog.rest.product.ProductRepresentation;
import org.seedstack.seed.it.SeedITRunner;
import org.seedstack.seed.rest.hal.HalRepresentation;
import org.seedstack.seed.rest.hal.Link;

import javax.inject.Inject;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@RunWith(SeedITRunner.class)
public class ProductAssemblerIT {

    @Inject
    public FluentAssembler fluently;

    @Test
    public void test_assemble() {
        Product product = new Product("productName");
        HalRepresentation representation = fluently.assemble(product).to(ProductRepresentation.class);

        Assertions.assertThat(representation).isNotNull();
        Assertions.assertThat(((Link) representation.getLink("self")).getHref()).isEqualTo("/products/productName");
        Assertions.assertThat(((Link) representation.getLink("tags")).getHref()).isEqualTo("/products/productName/tags");
    }
}
