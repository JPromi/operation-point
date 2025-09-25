function sortTable(columnIndex) {
  const table = document.getElementById("sortTable");
  const tbody = table.tBodies[0];
  const rows = Array.from(tbody.rows);
  const headers = table.querySelectorAll("th");

  // Aktuelle Sortierrichtung holen
  const currentHeader = headers[columnIndex];
  const isAscending = currentHeader.classList.contains("asc");

  // Alle Header-Classes zurücksetzen
  headers.forEach(h => h.classList.remove("asc", "desc"));

  // Neue Richtung setzen
  currentHeader.classList.add(isAscending ? "desc" : "asc");

  // Sortieren
  rows.sort((a, b) => {
    const valA = a.cells[columnIndex].innerText.trim();
    const valB = b.cells[columnIndex].innerText.trim();

    const numA = parseFloat(valA.replace(",", ".")) || valA.toLowerCase();
    const numB = parseFloat(valB.replace(",", ".")) || valB.toLowerCase();

    if (numA < numB) return isAscending ? 1 : -1;
    if (numA > numB) return isAscending ? -1 : 1;
    return 0;
  });

  tbody.append(...rows);
}
