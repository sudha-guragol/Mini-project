<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>


<!-- note :get the link from https://datatables.net/ -->

<!-- Datatable plugin CSS file -->
    <link rel="stylesheet" href=
"https://cdn.datatables.net/1.10.22/css/jquery.dataTables.min.css" />
  
     <!-- jQuery library file -->
     <script type="text/javascript" 
     src="https://code.jquery.com/jquery-3.5.1.js">
     </script>
  
      <!-- Datatable plugin JS library file -->
     <script type="text/javascript" 
            src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js">
     </script>
     
     <script>
  
        /* Initialization of datatable */
        $(document).ready(function() {
            $('#mobilesTbl').DataTable({ });
        });
    </script>
</head>
<body>
	<form:form action="searchMobiles" modelAttribute="formObj"
		method="POST">
		<table>
			<tr>
				<td>Brand</td>
				<td><form:select path="brand">
						<form:option value="">-Select-</form:option>
						<form:options items="${brands}" />
					</form:select></td>



				<td>Ram</td>
				<td><form:select path="ram">
						<form:option value="">-Select-</form:option>
						<form:options items="${rams}" />
					</form:select></td>

				<td>Rating</td>
				<td><form:select path="rating">
						<form:option value="">-Select-</form:option>gs}" />
						<form:options items="${ratings}" />
					</form:select></td>

				<%-- 	<td>Price</td>
				<td><form:select path="price">
						<form:option value="">-Select-</form:option>

						<form:option value="10000"> <10000</form:option>
						<form:option value="20000"> <20000</form:option>
						<form:option value="30000"> <30000</form:option>
						<form:option value="40000"> <40000</form:option>
						<form:option value="50000"> <50000</form:option>
						<form:option value="60000"> <60000</form:option>
						<form:option value="70000"> <70000</form:option>
					</form:select></td> --%>

				<td><input type="submit" value="Search" /></td>
			</tr>

		</table>
	</form:form>

	<br />
	<br />

	<div>
		<table border="1" id="mobilesTbl">
			<thead>
				<tr>
					<td>Mobile Name</td>
					<td>Mobile Price</td>
					<td>Mobile Rating</td>
					<td>Mobile RAM</td>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${mobiles}" var="mobile">
					<tr>
						<td>${mobile.brand}</td>
						<td>${mobile.price}</td>
						<td>${mobile.rating}</td>
						<td>${mobile.ram}</td>
					</tr>
				</c:forEach>
			</tbody>
			</table>
			</div>
</body>
</html>