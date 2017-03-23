package repositories.users

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import repositories.users.tables.UserRoleTableImpl



/**
  * Created by hashcode on 2017/01/29.
  */
class UserRoleDatabase (override val connector: KeySpaceDef) extends Database[UserRoleDatabase](connector) {
  object userRoleTable extends UserRoleTableImpl with connector.Connector
}

object UserRoleDatabase extends UserRoleDatabase(DataConnection.connector)

trait UserRoleRepository  {
  def  database = UserRoleDatabase
}

