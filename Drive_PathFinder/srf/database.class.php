<?php

	class Database {
		private $conn="";
		private $result="";

		public function connection($host,$user,$password,$database,$debug){
			$this->conn=mysqli_connect($host,$user,$password,$database);
			$this->debug=$debug;
			if($this->debug==1){
				if (mysqli_connect_errno())
				  {
				  echo "Failed to connect to MySQL: " . mysqli_connect_error();
				  }
			}

		}//connection method ends


		public function getConnection()
		{
			return $this->conn;
			
		}

		public function query($query){
			$this->result=mysqli_query($this->conn,$query);
			if($this->debug==1){
				if (!$this->result)
				  {
				 // echo "Query:".$query.'<br>';
				  echo "Failed to place a query" . mysqli_connect_error();
				  echo "<br>Error Detail:".mysqli_error ( $this->conn );
				  }
			}
			return mysqli_error($this->conn);
		}



		public function fetchRow($columns='*',$table,$where){
			$query='SELECT '.$columns.' FROM '.$table.' WHERE '.$where;

			$this->result=mysqli_query($this->conn,$query);
			
			if($this->debug==1){
				if (!$this->result)
				  {
				  echo "Query:".$query.'<br>';
				  echo "Failed to place a query" . mysqli_connect_error();
				  }
			}

			return mysqli_fetch_assoc($this->result);

		}

		public function fetchAllRows($query)

		{
			$this->result=mysqli_query($this->conn,$query);

			if($this->debug==1){
				if (!$this->result)
				  {
				  echo "Failed to place a query ".$query . mysqli_connect_error();
				  }
			}

			$allrows="";

			$i=0;

			while($row=mysqli_fetch_assoc($this->result))

			{

				$allrows[$i]=$row;
				//print_r($row);
				$i++;

				}

			return $allrows;

		}

		public function close()
		{
		mysqli_close($this->conn);
		}

		public function dbString($input)
		{
		return $this->conn->real_escape_string($input);
		}

		public function getInsertedRecordId()
		{
			return $this->conn->insert_id;
		}
		
		public function getEffectedRows(){
			return mysqli_affected_rows ($this->conn);
		}
		
		
	}//class end


?>
