(ns clj-mcp.core
  (:require
   [clj-mcp.convert :as convert])
  (:import
   (com.fasterxml.jackson.databind ObjectMapper)
   (io.modelcontextprotocol.server McpServer)
   (io.modelcontextprotocol.server.transport StdioServerTransportProvider)
   (io.modelcontextprotocol.spec McpSchema$ServerCapabilities)
   (java.util List)))

(def ^:private transport-provider
  (StdioServerTransportProvider. (ObjectMapper.)))

(def ^:private capabilities
  (-> (McpSchema$ServerCapabilities/builder)
      (.tools true)
      (.logging)
      (.build)))

(defn start
  [{:keys [name version tool-vars]}]
  (-> (McpServer/sync transport-provider)
      (.serverInfo name version)
      (.capabilities capabilities)
      (.tools (->> tool-vars
                   (map convert/convert-fn-var-to-tool-spec)
                   (apply List/of)))
      (.build)))
