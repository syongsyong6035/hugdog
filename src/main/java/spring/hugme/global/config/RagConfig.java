package spring.hugme.global.config;

import com.mongodb.client.MongoClient;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;

import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.mongodb.IndexMapping;
import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

  @Bean
  public EmbeddingModel embeddingModel(@Value("${langchain4j.google-ai-gemini.chat-model.api-key}") String apiKey) {
    return GoogleAiEmbeddingModel.builder()
        .apiKey(apiKey)
        .modelName("text-embedding-004")
        .build();
  }

  @Bean
  public MongoDbEmbeddingStore embeddingStore(MongoClient mongoClient) {

    Set<String> metadataFields = new HashSet<>();
    metadataFields.add("dogId");

    return MongoDbEmbeddingStore.builder()
        .fromClient(mongoClient)
        .databaseName("hugme_db")
        .collectionName("dog_memory")
        .indexName("vector_index")
        .createIndex(true)
        .indexMapping(IndexMapping.builder()
            .dimension(384)
            .metadataFieldNames(metadataFields) // [중요] 필터링할 필드 등록
            .build())
        .build();
  }

  @Bean
  public RetrievalAugmentor retrievalAugmentor(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {

    // 1. 기본 검색기 생성 (여기서 필터를 정의하지 않음)
    EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(3)
        .minScore(0.6)
        .build();

    return DefaultRetrievalAugmentor.builder()
        .contentRetriever(contentRetriever)
        .build();
  }

}
