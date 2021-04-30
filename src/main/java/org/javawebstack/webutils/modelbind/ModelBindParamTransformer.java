package org.javawebstack.webutils.modelbind;

import org.javawebstack.httpserver.transformer.route.DefaultRouteParamTransformer;
import org.javawebstack.orm.Model;
import org.javawebstack.orm.ORM;
import org.javawebstack.orm.Repo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModelBindParamTransformer extends DefaultRouteParamTransformer {

    private ModelBindTransformer transformer;
    private String accessorAttribName;

    public ModelBindParamTransformer() {
        super();
        this.transformer = (exchange, repo, fieldName, source) -> {
            Map<Class<? extends Model>, Map<Object, Object>> cache = exchange.attrib("__modelbindcache__");
            if(cache == null) {
                cache = new HashMap<>();
                exchange.attrib("__modelbindcache__", cache);
            }
            if(!cache.containsKey(repo.getInfo().getModelClass()))
                cache.put(repo.getInfo().getModelClass(), new HashMap<>());
            Map<Object, Object> modelCache = cache.get(repo.getInfo().getModelClass());
            Object model = modelCache.get(source);
            if(model == null) {
                model = repo.accessible(accessorAttribName == null ? null : exchange.attrib(accessorAttribName)).where(fieldName, source).first();
                modelCache.put(source, model);
            }
            return model;
        };
        for (Class<? extends Model> model : ORM.getModels()) {
            ModelBind[] binds = model.getDeclaredAnnotationsByType(ModelBind.class);
            if (binds.length == 0)
                continue;
            Repo<?> repo = Repo.get(model);
            String fieldName = binds[0].field().length() > 0 ? binds[0].field() : repo.getInfo().getIdField();
            Class<?> fieldType = repo.getInfo().getField(fieldName).getType();
            String parent = "string";
            if (fieldType.equals(UUID.class))
                parent = "uuid";
            if (fieldType.equals(Integer.class))
                parent = "i+";
            if (fieldType.equals(Long.class))
                parent = "l+";
            extend(parent, binds[0].value(), (exchange, source) -> transformer.transform(exchange, repo, fieldName, source));
        }
    }

    public void setTransformer(ModelBindTransformer transformer) {
        this.transformer = transformer;
    }

    public void setAccessorAttribName(String accessorAttribName) {
        this.accessorAttribName = accessorAttribName;
    }
}
