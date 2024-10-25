package coliver.dao.metro

import coliver.model.Metro

interface DAOFacade {
    suspend fun getAll(): List<Metro>
}
