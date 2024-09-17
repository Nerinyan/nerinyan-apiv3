package moe.nerinyan.apiv3.datasource.opensearch.repository

import moe.nerinyan.apiv3.datasource.opensearch.entity.OsBeatmapEntity
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository

interface OsBeatmapRepository: ReactiveElasticsearchRepository<OsBeatmapEntity, String> {

}