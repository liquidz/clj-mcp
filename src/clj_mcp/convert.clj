(ns clj-mcp.convert
  (:import
   (io.modelcontextprotocol.server McpServerFeatures$SyncToolSpecification)
   (io.modelcontextprotocol.spec McpSchema$CallToolResult McpSchema$JsonSchema McpSchema$TextContent McpSchema$Tool)
   (java.util List Map)
   (java.util.function BiFunction)))

(defn convert-fn-var-to-tool-spec
  [fn-var]
  (let [{:keys [name arglists doc]} (meta fn-var)
        arglist (first arglists)
        schema (McpSchema$JsonSchema.
                 ;; type
                 "object"
                 ;; properties
                 (Map/of)
                 ;; required properties
                 (->> (map str arglist)
                      (apply List/of))
                 ;; additional properties
                 nil)
        tool (McpSchema$Tool. (str name) doc schema)]
    (McpServerFeatures$SyncToolSpecification.
      tool
      (reify BiFunction
        (apply [_ _exchange arguments]
          (let [result (->> arglist
                            (map #(get arguments (str %)))
                            (apply @fn-var))
                content (McpSchema$TextContent. result)]
            (McpSchema$CallToolResult. (List/of content) false)))))))
