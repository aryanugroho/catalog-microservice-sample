/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.catalog.infrastructure.finder;

import com.google.inject.Inject;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.business.view.Page;
import org.seedstack.business.view.PaginatedView;
import org.seedstack.jpa.BaseJpaRangeFinder;
import org.seedstack.samples.catalog.Config;
import org.seedstack.samples.catalog.domain.product.Product;
import org.seedstack.samples.catalog.rest.product.ProductRepresentation;
import org.seedstack.samples.catalog.rest.tags.TagFinder;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@Named("tag")
@Transactional
@JpaUnit(Config.JPA_UNIT)
class TagJPAFinder extends BaseJpaRangeFinder<ProductRepresentation> implements TagFinder {

    public static final String TAG_NAME = "tagName";
    @Inject
    private EntityManager entityManager;

    @Inject
    private FluentAssembler fluently;

    @Override
    public PaginatedView<ProductRepresentation> findProductsByTag(Page page, String tagName) {
        Map<String, Object> map = new HashMap<>();
        if (tagName != null && !"".equals(tagName)) {
            map.put(TAG_NAME, tagName);
        }
        Result<ProductRepresentation> result = find(Range.rangeFromPageInfo(page.getIndex(), page.getCapacity()), map);
        return new PaginatedView<>(result, page);
    }

    @Override
    protected List<ProductRepresentation> computeResultList(Range range, Map<String, Object> criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> q = cb.createQuery(Product.class);
        String tagName = (String) criteria.get(TAG_NAME);
        Root<Product> root = q.from(Product.class);
        q.select(root)
                .where(cb.isMember(tagName, root.<List<String>>get("tags")));

        List<Product> products;
        if (range != null) {
            products = entityManager.createQuery(q).setFirstResult((int) range.getOffset()).setMaxResults((int) range.getSize()).getResultList();
        } else {
            products = entityManager.createQuery(q).getResultList();
        }

        return fluently.assemble(products).to(ProductRepresentation.class);
    }

    @Override
    protected long computeFullRequestSize(Map<String, Object> criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Product> root = q.from(Product.class);
        String tagName = (String) criteria.get(TAG_NAME);
        q.select(cb.count(root))
                .where(cb.isMember(tagName, root.<List<String>>get("tags")));

        return entityManager.createQuery(q).getSingleResult();
    }
}
