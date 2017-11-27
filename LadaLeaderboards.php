	<?php
		require_once("db.inc"); //Täällä on yhteyden muodostukseen tarvittavat määrittelyt
		$sqlQuery = "SELECT name, points FROM id ORDER BY points DESC";
		$tulos = mysqli_query($conn, $sqlQuery);
		$iterator = 0;
		if(!$tulos){
			echo "Tapahtui virhe! <br>".mysqli_error($conn);
		}else{
			echo "<table border>";
			echo "<tr><td><b></b></td><td><b>Name</b></td><td><b>Points</b></td></tr>";
			
			while ($rivi = mysqli_fetch_array($tulos, MYSQL_ASSOC)){
				$iterator++;
				$name = $rivi["name"];
				$points = $rivi["points"]; 

				echo "<tr><td>$iterator</td><td>$name</td><td>$points</td></tr>";
			}
			
			echo "</table>";
			}
	?>