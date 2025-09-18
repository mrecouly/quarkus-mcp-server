package io.quarkiverse.mcp.server.runtime;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.mcp.server.CompletionResponse;
import io.quarkiverse.mcp.server.JsonRpcErrorCodes;
import io.quarkiverse.mcp.server.McpException;
import io.quarkiverse.mcp.server.ResourceTemplateCompletionManager;
import io.quarkiverse.mcp.server.runtime.ResourceTemplateManagerImpl.ResourceTemplateMetadata;
import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.vertx.core.Vertx;

@Singleton
public class ResourceTemplateCompletionManagerImpl extends CompletionManagerBase implements ResourceTemplateCompletionManager {

    private final ResourceTemplateManagerImpl resourceTemplateManager;

    ResourceTemplateCompletionManagerImpl(McpMetadata metadata, Vertx vertx, ObjectMapper mapper,
            ConnectionManager connectionManager, ResourceTemplateManagerImpl resourceTemplateManager,
            Instance<CurrentIdentityAssociation> currentIdentityAssociation, ResponseHandlers responseHandlers) {
        super(vertx, mapper, connectionManager, currentIdentityAssociation, responseHandlers);
        for (FeatureMetadata<CompletionResponse> c : metadata.resourceTemplateCompletions()) {
            String key = c.info().name() + "_"
                    + c.info().arguments().stream().filter(FeatureArgument::isParam).findFirst().orElseThrow()
                            .name();
            this.completions.put(key, new CompletionMethod(c));
        }
        this.resourceTemplateManager = resourceTemplateManager;
    }

    @Override
    protected McpException notFound(String id) {
        return new McpException("Resource template completion does not exist: " + id, JsonRpcErrorCodes.INVALID_PARAMS);
    }

    @Override
    protected Feature feature() {
        return Feature.RESOURCE_TEMPLATE_COMPLETE;
    }

    @Override
    protected void validateReference(String refName, String argumentName) {
        ResourceTemplateMetadata templateMeta = resourceTemplateManager.templates.get(refName);
        if (templateMeta == null) {
            throw new IllegalStateException("Resource template does not exist: " + refName);
        }
        if (!templateMeta.variableMatcher().variables().contains(argumentName)) {
            throw new IllegalStateException("Resource template [" + refName + "] does not define argument: " + argumentName);
        }
    }

}
