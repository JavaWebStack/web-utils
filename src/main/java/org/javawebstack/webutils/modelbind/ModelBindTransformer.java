package org.javawebstack.webutils.modelbind;

import org.javawebstack.http.router.Exchange;
import org.javawebstack.orm.Repo;

public interface ModelBindTransformer {
    Object transform(Exchange exchange, Repo<?> repo, String fieldName, Object source);
}
