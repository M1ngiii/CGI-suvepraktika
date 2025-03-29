<script lang="ts">
    import { onMount } from 'svelte';
    import type { Flight } from '$lib/types';

    let flights: Flight[] = [];
    let from = '';
    let to = '';
    let minPrice = '';
    let maxPrice = '';
    let dateFrom = '';
    let dateTo = '';

    async function fetchFlights() {
        const params = new URLSearchParams();

        if (from) params.append('from', from);
        if (to) params.append('to', to);
        if (minPrice) params.append('minPrice', minPrice.toString());
        if (maxPrice) params.append('maxPrice', maxPrice.toString());
        if (dateFrom) params.append('dateFrom', new Date(dateFrom).toISOString());
        if (dateTo) params.append('dateTo', new Date(dateTo).toISOString());

        const res = await fetch(`${import.meta.env.VITE_API_BASE}/flights?${params.toString()}`);
        const data = await res.json();

        flights = data
            .map((f: any): Flight => ({
                ...f,
                departureTime: new Date(f.departureTime)
            }))
            .sort((a, b) => a.departureTime.getTime() - b.departureTime.getTime());
    }

    onMount(fetchFlights);
</script>

<h1 class="text-2xl font-bold mb-4">Lennuplaan</h1>

<form on:submit|preventDefault={fetchFlights} class="space-y-2 mb-6">
    <!-- Lähte- ja sihtkoha filtrid -->
    <div class="flex gap-2">
        <label class="flex flex-col text-sm text-gray-700 w-full">
            Lähtekoht
            <input
                    placeholder="from"
                    bind:value={from}
                    class="border p-2 rounded w-full"
            />
        </label>
        <label class="flex flex-col text-sm text-gray-700 w-full">
            Sihtkoht
            <input
                    placeholder="to"
                    bind:value={to}
                    class="border p-2 rounded w-full"
            />
        </label>
    </div>

    <!-- Kuupäeva filtrid -->
    <div class="flex items-center gap-2">
        <label class="flex flex-col text-sm text-gray-700 w-full">
            Alates
            <input
                    type="date"
                    bind:value={dateFrom}
                    class="border p-2 rounded w-full"
            />
        </label>
        <label class="flex flex-col text-sm text-gray-700 w-full">
            Kuni
            <input
                    type="date"
                    bind:value={dateTo}
                    class="border p-2 rounded w-full"
            />
        </label>
    </div>

    <!-- Hinnafiltrid -->
    <div class="flex gap-2">
        <label class="flex flex-col text-sm text-gray-700 w-full">
            Miinimum hind (€)
            <input
                    placeholder="min"
                    type="number"
                    bind:value={minPrice}
                    class="border p-2 rounded w-full"
            />
        </label>
        <label class="flex flex-col text-sm text-gray-700 w-full">
            Maksimum hind (€)
            <input
                    placeholder="max"
                    type="number"
                    bind:value={maxPrice}
                    class="border p-2 rounded w-full"
            />
        </label>
    </div>

    <!-- Submit nupp -->
    <button type="submit" class="bg-sky-500 text-white py-2 px-4 rounded hover:bg-blue-600">
        Otsi
    </button>
</form>

{#if flights.length === 0}
    <p>Ühtegi lendu ei leitud.</p>
{:else}
    <ul class="space-y-4">
        {#each flights as flight}
            <a href={`/flights/${flight.id}`} class="block border p-4 rounded shadow bg-gray-100 hover:bg-gray-200 transition">
                <li>
                    <p><strong>{flight.from}</strong> → <strong>{flight.to}</strong></p>
                    <p>Väljumine: {new Date(flight.departureTime).toLocaleString()}</p>
                    <p>Hind: <strong>{flight.price} €</strong></p>
                </li>
            </a>
        {/each}
    </ul>
{/if}
