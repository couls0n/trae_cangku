<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background: #409EFF">
            <i class="el-icon-office-building"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.warehouseCount }}</div>
            <div class="stat-label">仓库数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background: #67C23A">
            <i class="el-icon-goods"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.productCount }}</div>
            <div class="stat-label">商品种类</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background: #E6A23C">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.inboundCount }}</div>
            <div class="stat-label">入库单数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background: #F56C6C">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.outboundCount }}</div>
            <div class="stat-label">出库单数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">快捷操作</div>
          <div class="quick-actions">
            <el-button type="primary" icon="el-icon-plus" @click="$router.push('/inbound')">入库登记</el-button>
            <el-button type="success" icon="el-icon-minus" @click="$router.push('/outbound')">出库登记</el-button>
            <el-button type="warning" icon="el-icon-search" @click="$router.push('/stock')">库存查询</el-button>
            <el-button type="info" icon="el-icon-goods" @click="$router.push('/product')">商品管理</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">系统信息</div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统名称">仓库管理系统</el-descriptions-item>
            <el-descriptions-item label="版本号">V1.0.0</el-descriptions-item>
            <el-descriptions-item label="技术栈">Spring Boot + Vue + ElementUI</el-descriptions-item>
            <el-descriptions-item label="数据库">MySQL</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        warehouseCount: 0,
        productCount: 0,
        inboundCount: 0,
        outboundCount: 0
      }
    }
  },
  created() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const [warehouse, product, inbound, outbound] = await Promise.all([
          request.get('/warehouse/list'),
          request.get('/product/list'),
          request.get('/inbound/list'),
          request.get('/outbound/list')
        ])
        this.stats.warehouseCount = warehouse.data.length
        this.stats.productCount = product.data.length
        this.stats.inboundCount = inbound.data.length
        this.stats.outboundCount = outbound.data.length
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

.stat-icon i {
  font-size: 30px;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
